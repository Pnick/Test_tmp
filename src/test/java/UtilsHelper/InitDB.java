package UtilsHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class InitDB {

	MongoClient mongoClient;
	MongoDatabase database;

	public InitDB(String environment) {

		//List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
		//databases.forEach(db -> System.out.println(db.toJson()));

		MongoClientURI uriString = getURI(environment);

		try {
			mongoClient = new MongoClient(uriString);
			System.out.println("Connect to host : " + uriString);

			database = mongoClient.getDatabase(setDB(environment));
			System.out.println("Connect to DB : " + database);

		} catch (Exception e) {
			System.out.println("Cannot connect to " + uriString);
			e.printStackTrace();
		}
	}

	protected MongoClientURI getURI(String environment) {

		String uri = PropertyUtils.getProperty( environment + ".db");

		MongoClientURI connectionString = new MongoClientURI(uri);

		return connectionString;
	}

	protected String setDB(String environment){

		String dataBase = "rgs_" + environment;

		return dataBase;
	}

	public MongoCollection<Document> setCollection(String collectionName, CommonVariables var) {

		var.collection = database.getCollection(collectionName);
		System.out.println("Set DB collection = " + var.collection.getNamespace());

		try {
			var.cursor = var.collection.find().iterator();
		}
		catch(Exception e) {System.out.println("WARNING : Cannot connect to DB!");}

		return var.collection;
	}

	public long printResultsFilter(Bson filter, CommonVariables var){

		Document str;
		long i = -1 ;
		long count = 0;

		try {
			count = var.collection.countDocuments(filter);
			//System.out.println("Number of " + var.collection.getNamespace() + " target rows = " + count);
		}
		catch(Exception e) {
			System.out.println("WARNING : Cannot connect to " + var.collection.getNamespace());
		}

		if(count == 0) {
			try {
				var.cursor = var.collection.find(filter).iterator();

				i = 0 ;
				while (var.cursor.hasNext()) {
					str = var.cursor.next();
					System.out.println(str.toJson());
					i++;
				}
			}
			catch(Exception e) {
				System.out.println("WARNING : Cannot connect to " + var.collection.getNamespace() + " OR cursor is null.");
			}
			finally {
				if(var.cursor != null) {
					var.cursor.close();
				}
			}
		}
		else {
			i = count;
		}
		return i;
	}

	public void insertDocAsJSON(String tableName, List<Integer> insertIds, CommonVariables var){

		var.collection.insertMany(new ModelsDB().addDocuments(tableName, insertIds));

		//        Document doc = Document.parse(json);
		//        collection.insertOne(doc);
	}

	public void updateDoc(CommonVariables var, String tableName, Bson document, String key, Object value) {

		CommonVariables.db.setCollection(tableName, var);

		var.collection.updateOne(document, Updates.set(key, value));
		//var.collection.updateOne(filter, new Document("$set", new Document(key, value)));
	}

	public void insertDoc(CommonVariables var, String tableName, Document document) {

		CommonVariables.db.setCollection(tableName, var);

		var.collection.insertOne(document);
	}

	public Object getValue(CommonVariables var, String tableName, Bson filter, String targetValue) {

		CommonVariables.db.setCollection(tableName, var);

		List<String> nestedDocuments = Arrays.asList(targetValue.split("\\."));

		Document document = var.collection.find(filter).first();

		if(document==null) {
			return -1;
		}
		else {
			return document.getEmbedded(nestedDocuments, -1);
		}
	}

	public void deleteDoc(CommonVariables var, String tableName, Bson document) {

		CommonVariables.db.setCollection(tableName, var);

		var.collection.deleteOne(document);
	}

	public HashMap<String, Object> getGameDataFromDB(CommonVariables var,String game, String operator, String currency, String config) {

		Object betConfigId = null, configPerOperatorId = null;
		HashMap<String, Object>	gameDataOnDB = new HashMap<String, Object>();

		gameDataOnDB.put("gameId", getValue(var, "games" , Filters.regex("name", "^" + game + "$", "i") , "_id"));
		gameDataOnDB.put("gameType", getValue(var, "games" , Filters.regex("name", "^" + game + "$", "i") , "type"));
		gameDataOnDB.put("operatorId", getValue(var, "operators" , Filters.eq("name", operator) , "_id"));

		try {
			betConfigId = getValue(var, "games_per_operator" ,
					Filters.and(Filters.eq("operator_id", gameDataOnDB.get("operatorId")),
							Filters.eq("game_id", gameDataOnDB.get("gameId"))) , "bet_config_id");
		}catch(Exception e) {}

		if(betConfigId != null && betConfigId != "null") {
			if(ObjectId.isValid(betConfigId.toString()) && new ObjectId(betConfigId.toString()).equals(betConfigId)) {
				betConfigId = new ObjectId(betConfigId.toString());
			}
		}

		gameDataOnDB.put("betConfigId", betConfigId);

		try {
			configPerOperatorId = getValue(var, "bet_configurations" ,
					Filters.eq("_id", gameDataOnDB.get("operatorId")) , "_id");
		}catch(Exception e) {}

		gameDataOnDB.put("configPerOperatorId", configPerOperatorId);

		List<Integer>currentStakes = null;
		Integer currentMaxCap = null;

		String documentCapPath = "per_currency." + currency.toUpperCase() + ".max_payout_cap";
		Object defaultMaxCap = CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", gameDataOnDB.get("gameType")), documentCapPath);

		String documentStakesPath = "per_currency." + currency.toUpperCase() + ".bet_values";
		Object defaultStakes = CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", gameDataOnDB.get("gameType")), documentStakesPath);

		if(config.equals("operator")) {

			String operatorCapPath = "per_game_type." + gameDataOnDB.get("gameType") + "." + currency.toUpperCase() + ".max_payout_cap";
			currentMaxCap = (Integer)CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", gameDataOnDB.get("operatorId")), operatorCapPath);

			String operatorPath = "per_game_type." + gameDataOnDB.get("gameType") + "." + currency.toUpperCase() + ".bet_values";
			Object operatorStakes = CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", gameDataOnDB.get("operatorId")), operatorPath);

			currentStakes = Arrays.asList(operatorStakes.toString().split(",")).stream().map(s ->  Integer.parseInt(s.trim().replace("[", "").replace("]", ""))).collect(Collectors.toList());
			currentStakes.sort(Comparator.naturalOrder());
		}
		else if(config.equals("game")) {

			currentMaxCap = (Integer)CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", betConfigId), documentCapPath);

			Object gameStakes = CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", betConfigId), documentStakesPath);

			currentStakes = Arrays.asList(gameStakes.toString().split(",")).stream().map(s ->  Integer.parseInt(s.trim().replace("[", "").replace("]", ""))).collect(Collectors.toList());
			currentStakes.sort(Comparator.naturalOrder());
		}
		else {
			currentMaxCap = (Integer)defaultMaxCap;

			currentStakes = Arrays.asList(defaultStakes.toString().split(",")).stream().map(s ->  Integer.parseInt(s.trim().replace("[", "").replace("]", ""))).collect(Collectors.toList());
			currentStakes.sort(Comparator.naturalOrder());
		}

		gameDataOnDB.put("defaultMaxCap", defaultMaxCap);
		gameDataOnDB.put("defaultStakes", defaultStakes);
		gameDataOnDB.put("currentMaxCap", currentMaxCap);
		gameDataOnDB.put("currentStakes", currentStakes);
		gameDataOnDB.put("documentCapPath", documentCapPath);
		gameDataOnDB.put("documentStakesPath", documentStakesPath);

		return gameDataOnDB;
	}

	public int getExistingCap(CommonVariables var, HashMap<String, Object> gameDataOnDB, String currency, String config) {

		String documentCapPath	= "NO RECORD";
		Integer existingCap		=	null;

		try {
			if(config.equals("operator")) {
				documentCapPath = "per_game_type." + gameDataOnDB.get("gameType") + "." + currency + ".max_payout_cap";
				existingCap = (int)CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", gameDataOnDB.get("operatorId")), documentCapPath);
			}
			else if(config.equals("game")) {
				documentCapPath = "per_currency." + currency + ".max_payout_cap";
				existingCap = (int)CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", gameDataOnDB.get("betConfigId")), documentCapPath);
			}
			else if(config.equals("default")) {
				documentCapPath = "per_currency." + currency + ".max_payout_cap";
				existingCap = (int)CommonVariables.db.getValue(var, "bet_configurations", Filters.eq("_id", gameDataOnDB.get("gameType")), documentCapPath);	
			}
		}catch(Exception e) {}

		System.out.println("Existing payout cap for " + config + " on DB " + documentCapPath + " = " + existingCap);

		if(existingCap == null) {
			existingCap = 0;
		}
		return existingCap;
	}

	public void setBetConfigDataOnDB(CommonVariables var, HashMap<String, Object> gameDataOnDB, String currency, String config, HashMap<String, Object>	newData) throws UnsupportedOperationException, IOException {

		Object defaultInstantCap = null;
		Object defaultInstantStakes = null;
		int newCap = 0;
		Object newBets = null;
		Object newBetConfigId = "";

		defaultInstantStakes = Arrays.asList(10, 1, 2, 5, 20, 50, 100, 200, 2000);
		defaultInstantCap = 10000000;

		if((boolean) newData.get("setMaxCap") && (boolean) newData.get("setStakes")){
			newCap = (int)newData.get("newMaxCap");
			newBets = newData.get("newStakes");
		}
		else if((boolean) newData.get("setMaxCap")==false && (boolean) newData.get("setStakes")) {
			newCap = (int) gameDataOnDB.get("currentMaxCap");
			newBets = newData.get("newStakes");
		}
		else if((boolean) newData.get("setStakes")==false && (boolean) newData.get("setMaxCap")) {
			newCap = (int)newData.get("newMaxCap");
			newBets = gameDataOnDB.get("currentStakes");		//Arrays.asList(1,10,20,50,100,200);
		}
		else {
			newCap = (int) gameDataOnDB.get("currentMaxCap");
			newBets = gameDataOnDB.get("currentStakes");
		}

		if(!var.collection.getNamespace().toString().contains("bet_configurations")) {
			CommonVariables.db.setCollection("bet_configurations", var);
		}

		if(gameDataOnDB.get("defaultStakes").toString().equals("-1") || gameDataOnDB.get("defaultMaxCap").toString().equals("-1")) {
			var.collection.updateOne(Filters.eq("_id", gameDataOnDB.get("gameType")),
					new BasicDBObject("$set", new BasicDBObject()
							.append("per_currency."+currency
									, new Document()
									.append("bet_values", defaultInstantStakes)
									.append("max_payout_cap", (int) gameDataOnDB.get("currentMaxCap")))));
			defaultInstantCap = (int) gameDataOnDB.get("currentMaxCap");
		}

		if((boolean) newData.get("setMaxCap") || (boolean) newData.get("setStakes")){

			// DELETE all the records for game in tables : games_per_operator and bet_configurations
			if(gameDataOnDB.get("betConfigId") != null && (!gameDataOnDB.get("betConfigId").toString().isEmpty())) {
				deleteDoc( var,  "bet_configurations",  Filters.eq("_id", gameDataOnDB.get("betConfigId")));
			}
			if(gameDataOnDB.get("configPerOperatorId") != null) {
				deleteDoc( var,  "bet_configurations",  Filters.eq("_id", gameDataOnDB.get("configPerOperatorId")));
			}

			// UPDATE/INSERT new records for specific tests into tables : games_per_operator and bet_configurations
			if(config.equals("default")){

				//gameDataOnDB.replace("betConfigId", "");
				newBetConfigId = "";

				if(newCap != (int)defaultInstantCap || newBets != defaultInstantStakes) {
					var.collection.updateOne(Filters.eq("_id", gameDataOnDB.get("gameType")),
							new BasicDBObject("$set", new BasicDBObject()
									.append("per_currency."+currency
											, new Document()
											.append("bet_values", newBets)
											.append("max_payout_cap", newCap))));

					System.out.println("Update record into 'bet_configurations' table :");
					System.out.println("New default Instant Stake Values = " + newBets.toString());
					System.out.println("New default Instant Cap Value = " + newCap);
				}
			}
			else {
				if(!gameDataOnDB.get("defaultStakes").toString().equals(defaultInstantStakes)
						|| !gameDataOnDB.get("defaultMaxCap").toString().equals(defaultInstantCap)) {

					var.collection.updateOne(Filters.eq("_id", gameDataOnDB.get("gameType")),
							new BasicDBObject("$set", new BasicDBObject()
									.append("per_currency."+currency 
											, new Document()
											.append("bet_values", defaultInstantStakes)
											.append("max_payout_cap", defaultInstantCap))));

					System.out.println("Update record into 'bet_configurations' table :");
					System.out.println("New default Instant Stake Values = " + defaultInstantStakes.toString());
					System.out.println("New default Instant Cap Value = " + defaultInstantCap);
				}

				if(config.equals("operator")) {
					//gameDataOnDB.replace("betConfigId", "");
					newBetConfigId = "";

					insertDoc( var, "bet_configurations"
							, new Document("_id", gameDataOnDB.get("operatorId"))
							.append("per_game_type"
									, new Document()
									.append(gameDataOnDB.get("gameType").toString()
											, new Document()
											.append(currency
													, new Document()
													.append("bet_values", newBets)
													.append("max_payout_cap", newCap)))					
									));
					System.out.println("Insert record for operator into 'bet_configurations' table :");
					System.out.println("New values : max_payout_cap = " + newCap);
					System.out.println("New values : bet_values = " + newBets.toString());
				}
				else if(config.equals("game")) {
					//gameDataOnDB.replace("betConfigId", new ObjectId());
					newBetConfigId = new ObjectId();

					insertDoc( var, "bet_configurations"
							, new Document("_id", newBetConfigId)
							.append("per_currency"
									, new Document()
									.append(currency
											, new Document()
											.append("bet_values", newBets)
											.append("max_payout_cap", newCap)))					
							);
					System.out.println("Insert record for game into 'bet_configurations' table : ");
					System.out.println("New values : max_payout_cap = " + newCap);
					System.out.println("New values : bet_values = " + newBets.toString());
				}
			}

			if(gameDataOnDB.get("betConfigId")==null) {
				insertDoc( var, "games_per_operator"
						, new Document("_id", new ObjectId())
						.append("operator_id", gameDataOnDB.get("operatorId"))
						.append("game_id", gameDataOnDB.get("gameId"))
						.append("bet_config_id", newBetConfigId));
			}
			else {
				updateDoc( var, "games_per_operator"
						, new Document().append("operator_id", gameDataOnDB.get("operatorId")).append("game_id", gameDataOnDB.get("gameId"))
						, "bet_config_id"
						, newBetConfigId);
			}
			System.out.println("Update record on 'games_per_operator' table.");
		}
	}
}
