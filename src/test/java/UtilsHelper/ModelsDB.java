package UtilsHelper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bson.Document;

import com.mongodb.BasicDBObject;

public class ModelsDB {

	List<Document> listDocuments = new ArrayList<>();
	private Integer idGame, idProvider, idOperator, idHistory, idUsers;
	private Integer idType, idCategories, idRgsInstance;
	private Integer rtp, totalBetAmount, totalWinAmount, uncappedTotalWin;
	private String nameGame, shortName, nameType, nameCategory, typeGame, nameProvider, typeProvider;
	private String nameOperator, nameIntegrationType, nameIntegrationPlatform, idPlayer, operatorURL;
	private String status, currency, gameUIurl, passHash, userName, name, email, company, timeZone, userRole, language;
	private long date;
	boolean isEnabled;

	public List<Document> addDocuments(String tableName,  List<Integer> insertIds) {

		for(int idNumber : insertIds) {

			setJsonValues(tableName, idNumber-1000000000);
			listDocuments.add(Document.parse(buildJsonDocument(tableName)));
		}
		return listDocuments;
	}

	public void setJsonValues(String tableName, int numDocuments) {

		setIdGame(numDocuments + 1000000000);
		setNameGame("GameName" + numDocuments);
		setShortName("gnsh" + numDocuments);
		setIdProvider(numDocuments + 1000000000);
		setNameProvider("Provider" + numDocuments);
		setIdOperator(numDocuments + 1000000000);
		setNameOperator("Operator" + numDocuments);
		setRtp(numDocuments*100);
		setEnabled(true);

		if(numDocuments<3) {
			setTypeGame("INSTANT_WIN");
		}
		else {
			setTypeGame("SLOT");
		}

		if(tableName.equals("games")) {
			setIdType(numDocuments);
			setNameType("Type" + numDocuments);
			setIdCategories(numDocuments);
			setNameCategory("Category" + numDocuments);
			setGameURL("https://someDomain.com/games/GameName" + numDocuments);
			if(numDocuments>11) {
				setIdProvider(1000000003);
			}
		}
		else if(tableName.equals("operators")) {
			setIdRgsInstance(numDocuments);
			setOperatorURL("http://operator" + numDocuments + ".com");
			if(numDocuments<10) {
				setNameIntegrationType("DIRECT");
				setNameIntegrationPlatform("INTEGRATION_PLATFORM_1");
			}
			else {
				//Random rand = new Random(); // rand.nextInt(2)
				if(numDocuments==10) {
					setNameIntegrationPlatform("INTEGRATION_PLATFORM_1");
				}
				else {
					setNameIntegrationPlatform("NGL_PLATFORM");
				}
				setNameIntegrationType("AGGREGATOR");
			}
		}
		else if(tableName.equals("game_history")) {

			boolean uniqueTime1 = false,uniqueTime2 = false;

			if(numDocuments<73) {
				uniqueTime1 = true;
			}
			else if(numDocuments>73 && numDocuments<109) {
				uniqueTime2 = true;
			}

			setIdHistory(numDocuments + 1000000000);

			numDocuments = numDocuments%12;
			if(numDocuments==0) {
				numDocuments = 12;
			}

			setIdGame(numDocuments + 1000000000);
			setNameGame("GameName" + numDocuments);
			setShortName("gnsh" + numDocuments);
			setIdProvider(numDocuments + 1000000000);
			setNameProvider("Provider" + numDocuments);
			setIdOperator(numDocuments + 1000000000);
			setNameOperator("Operator" + numDocuments);
			setIdPlayer("Player" + numDocuments);
			setTotalBetAmount(numDocuments);
			setTotalWinAmount(numDocuments);
			setUncappedTotalWin(numDocuments);
			setCurrency("GBP");
			//setTransactions();

			if(numDocuments==1) {
				if(getIdHistory()<1000000026) {
					setStatus("OPEN");
				}
				else if(1000000026<getIdHistory() && getIdHistory()<1000000086) {
					setStatus("CLOSED");
				}
				else{
					setStatus("CANCELLED");
				}
				
				if(uniqueTime1) {
					setDate(getDate(numDocuments).minusMinutes(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
				}
				else if(uniqueTime2) {
					setDate(getDate(numDocuments).plusMinutes(2).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
				}
				else {
					setDate(getDate(numDocuments).plusMinutes(4).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
				}
				
			}
			else {
				setDate(getDate(numDocuments).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
				setStatus("random");
			}
		}
		else if(tableName.equals("providers")) {

			setTypeProvider("GAME_TO_RGS");

		}else if(tableName.equals("users")) {

			setIdUsers(numDocuments + 1000000000);
			setName("nameuser" + numDocuments);
			setUserName("username" + numDocuments);
			setEmail("admin"  + numDocuments + "@admin.com");
			setCompany("MWS");
			setTimeZone("UTC_PLUS_0000");
			setLanguage("EN");
			setDate(getDate(numDocuments).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			
			if(numDocuments<11 || numDocuments>14) {
				setUserRole("ADMIN");
				setPassHash("acff0e59e00ae7ac61a757a1d93ef7f874c2e3d1b3099d554d808b59b714295e");
			}
			else {
				setUserRole("OPERATOR_GROUP_ACCOUNT");
				setPassHash("acff0e59e00ae7ac61a757a1d93ef7f874c2e3d1b3099d554d808b59b714295e");	
			}
			if(numDocuments==10) {
				setEnabled(false);
			}
		}
	}

	public String buildJsonDocument(String tableName) {
		String json = null;

		if(tableName.equals("game_management")) {
			json =  "{" + 
					"   \"id\":" + idGame + "," +
					"   \"name\":\"" + nameGame + "\"," +
					"	\"provider\": {" + 
					"   	\"id\":" + idProvider + "," +
					"   	\"name\":\"" + nameProvider + "\"" +
					"	}," + 
					"	\"type\": {" + 
					"   	\"id\":" + idType + "," +
					"   	\"name\":\"" + nameType + "\"" +
					"	}," + 
					"	\"categories\": [" + 
					"		{" + 
					"   		\"id\":" + idCategories + "," +
					"   		\"name\":\"" + nameCategory + "\"" +
					"		}," + 
					"				{" + 
					"			\"id\": 345," + 
					"			\"name\": \"Boutique\"" + 
					"		}," + 
					"		{" + 
					"			\"id\": 789," + 
					"			\"name\": \"Branded\"" + 
					"		}" + 
					"	]," + 
					"   \"rtp\":" + Double.valueOf(rtp)/100 + ""+  
					"}";
		}
		else if(tableName.equals("games")) {
			json =  "{" +
					"   \"_id\":" + idGame + "," +
					"   \"is_enabled\":" + true + "," +
					"   \"name\":\"" + nameGame + "\"," +
					"   \"short_name\":\"" + shortName + "\"," +
					"   \"provider_id\":" + idProvider + "," +
					"   \"game_ui_url\":\"" + gameUIurl + "\"," +
					"   \"type\":\"" + typeGame + "\"," +
					"	\"categories\":[\"PREMIUM\"]," +
					"   \"rtp\":" + rtp + ""+  
					"}";
		}
		else if(tableName.equals("operators")) {
			json =  "{" + 
					"   \"_id\":" + idOperator + "," +
					"   \"name\":\"" + nameOperator + "\"," +
					"   \"short_name\":\"" + nameOperator + "\"," +
					"   \"is_enabled\":" + true + "," +
					"   \"integration_platform\":\"" + nameIntegrationPlatform + "\"," +
					"   \"integration_type\":\"" + nameIntegrationType + "\"," +
					"   \"url\":\"" + operatorURL + "\"" +
					"}";
		}
		else if(tableName.equals("game_history")) {
			
			BasicDBObject dateEnded = null;
			
			if(!status.equals("OPEN")) {
				dateEnded = new BasicDBObject("$date", date);
			}
			
			json =  "{" +
					"   \"_id\":" + idHistory + "," +
					"    \"date_time_started\":" + new BasicDBObject("$date", date) + "," +
					"    \"date_time_ended\":" + dateEnded + "," +
					"    \"provider_id\":" + idProvider + "," + 
					"    \"provider_name\":\"" + nameProvider + "\"," +
					"    \"game_id\":" + idGame + "," + 
					"    \"game_name\":\"" + nameGame + "\"," +
					"    \"game_short_name\":\"" + shortName + "\"," +
					"    \"game_type\":\"" + typeGame + "\"," +
					"    \"operator_id\":" + idOperator + "," +
					"    \"operator_short_name\":\"" + nameOperator + "\"," +
					"    \"operator_name\":\"" + nameOperator + "\"," +
					"    \"status\": \"" + status + "\"," +
					"    \"player_id\":\"" + idPlayer+ "\"," +
					"    \"player_currency\":\"" + currency + "\"," +
					"    \"total_bet\":" + totalBetAmount + "," +
					"    \"total_win\":" + totalWinAmount + "," +
					"    \"uncapped_total_win\":" + uncappedTotalWin + "," + 
					"	 \"transactions\":[]," +
					"    \"state\":{" +
					"    		\"outcome\":{" +
					"					\"amount\":\"5000.00\","  + 
					"					\"seed\":\"504210511\","  +
					"					\"tier\":\"3\","  + 
					"					\"wT\":\"1\""  +
					"				}," +
					"    		\"params\":{" +
					"					\"gameWin\":\"0.00\"," +
					"					\"bonusWin\":\"5000.00\"," +
					"					\"turns\":" + new BasicDBObject() + "" +
					"				}" +
					"		}" + 
					"}";
		}
		else if(tableName.equals("providers")) {
			json =  "{" + 
					"   \"_id\":" + idProvider + "," +
					"   \"name\":\"" + nameProvider + "\"," +
					"   \"is_enabled\":" + true + "," +
					"   \"type\":\"" + typeProvider + "\"" +
					"}";
		}
		else if(tableName.equals("users")) {
			json =  "{" +
					"   \"_id\":" + idUsers + "," +
					"   \"is_enabled\":" + isEnabled + "," +
					"   \"is_email_confirmed\":" + true + "," +
					"   \"password_hash\":\"" + passHash + "\"," +
					"   \"user_name\":\"" + userName + "\"," +
					"   \"name\":\"" + name + "\"," +
					"   \"email\":\"" + email + "\"," +
					"   \"company\":\"" + company + "\"," +
					"   \"time_zone\":\"" + timeZone + "\"," +
					"   \"user_role\":\"" + userRole + "\"" +
					"	\"has_access_to_operator_ids\":[2]," +
					"   \"date_created\":" + new BasicDBObject("$date", date) + "," +
					"   \"date_last_updated\":" + new BasicDBObject("$date", date) + "," +
					"   \"language\":\"" + language + "\"" +
					"}";
					//optional at the moment ->	"   \"password_reset_hash\":" + null + "," +
		}

		return json;
	}

	public LocalDateTime getDate(int offset) {

		LocalDateTime dateNow = LocalDateTime.now().plusHours(offset);
		return dateNow;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public Integer getIdGame() {
		return idGame;
	}

	public void setIdGame(Integer idGame) {
		this.idGame = idGame;
	}

	public Integer getIdProvider() {
		return idProvider;
	}

	public void setIdProvider(Integer idProvider) {
		this.idProvider = idProvider;
	}

	public Integer getIdType() {
		return idType;
	}

	public void setIdType(Integer idType) {
		this.idType = idType;
	}

	public Integer getIdCategories() {
		return idCategories;
	}

	public void setIdCategories(Integer idCategories) {
		this.idCategories = idCategories;
	}

	public Integer getIdUsers() {
		return idUsers;
	}

	public void setIdUsers(Integer idUsers) {
		this.idUsers = idUsers;
	}

	public Integer getRtp() {
		return rtp;
	}

	public void setRtp(Integer rtp) {
		this.rtp = rtp;
	}

	public String getNameGame() {
		return nameGame;
	}

	public void setNameGame(String nameGame) {
		this.nameGame = nameGame;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getNameProvider() {
		return nameProvider;
	}

	public void setNameProvider(String nameProvider) {
		this.nameProvider = nameProvider;
	}

	public void setTypeProvider(String typeProvider) {
		this.typeProvider = typeProvider;
	}

	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public String getNameCategory() {
		return nameCategory;
	}

	public void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}

	public void setGameURL(String gameUIurl) {
		this.gameUIurl = gameUIurl;
	}

	public String getPassHash() {
		return passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getGameURL() {
		return gameUIurl;
	}

	public Integer getIdOperator() {
		return idOperator;
	}

	public void setIdOperator(Integer idOperator) {
		this.idOperator = idOperator;
	}

	public Integer getIdRgsInstance() {
		return idRgsInstance;
	}

	public void setIdRgsInstance(Integer idRgsInstance) {
		this.idRgsInstance = idRgsInstance;
	}

	public String getOperatorURL() {
		return operatorURL;
	}
	
	public void setOperatorURL(String operatorURL){
		this.operatorURL = operatorURL;
	}
	
	public String getNameOperator() {
		return nameOperator;
	}

	public void setNameOperator(String nameOperator) {
		this.nameOperator = nameOperator;
	}

	public String getNameIntegrationType() {
		return nameIntegrationType;
	}

	public void setNameIntegrationType(String nameIntegrationType) {
		this.nameIntegrationType = nameIntegrationType;
	}

	public String getNameIntegrationPlatform() {
		return nameIntegrationPlatform;
	}

	public void setNameIntegrationPlatform(String nameIntegrationPlatform) {
		this.nameIntegrationPlatform = nameIntegrationPlatform;
	}

	public Integer getIdHistory() {
		return idHistory;
	}

	public void setIdHistory(Integer idHistory) {
		this.idHistory = idHistory;
	}

	public String getTypeGame() {
		return typeGame;
	}

	public void setTypeGame(String typeGame) {
		this.typeGame = typeGame;
	}

	public String getIdPlayer() {
		return idPlayer;
	}

	public void setIdPlayer(String idPlayer) {
		this.idPlayer = idPlayer;
	}

	public Integer getTotalBetAmount() {
		return totalBetAmount;
	}

	public void setTotalBetAmount(Integer totalBetAmount) {
		this.totalBetAmount = totalBetAmount;
	}

	public Integer getTotalWinAmount() {
		return totalWinAmount;
	}

	public void setTotalWinAmount(Integer totalWinAmount) {
		this.totalWinAmount = totalWinAmount;
	}

	public Integer getUncappedTotalWin() {
		return uncappedTotalWin;
	}

	public void setUncappedTotalWin(Integer uncappedTotalWin) {
		this.uncappedTotalWin = uncappedTotalWin;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {

		if(status.equals("random")) {
			List<String> myList = Arrays.asList("OPEN","CLOSED","CANCELLED");
		    Random rnd = new Random();
			status = myList.get(rnd.nextInt(myList.size()));
		}
		this.status = status;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}
