package com.kamabizbazti;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.entities.Authority;
import com.kamabizbazti.security.entities.AuthorityName;
import com.kamabizbazti.security.entities.User;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class DataGenerator {
	private static final String NAMES_DATABASE = "/Users/macbookair13/Google Drive/My Java Projects/KamaBizbazti/Resources/15000NamesDatabase.txt";

	public void insertLanguages(LanguageRepository languageRepository) {
		long start = System.currentTimeMillis();
		Language lang = new Language("RUS", "Russian");
		Language lang1 = new Language("ENG", "English");
		Language lang2 = new Language("HEB", "Hebrew");
		languageRepository.save(lang);
		languageRepository.save(lang1);
		languageRepository.save(lang2);
		System.out.println(String.format("Languages loaded in %dms", System.currentTimeMillis()-start));
	}

		public void insertCurrencies(CurrencyRepository currencyRepository) {
			long start = System.currentTimeMillis();
		Currency currency = new Currency("RUB", "Russian Ruble");
		Currency currency1 = new Currency("USD", "US Dollar");
		Currency currency2 = new Currency("ILS", "Israeli New Sheqel");
		currencyRepository.save(currency);
		currencyRepository.save(currency1);
		currencyRepository.save(currency2);
			System.out.println(String.format("Currencies loaded in %dms", System.currentTimeMillis()-start));
	}

	public void insertPurposes(GeneralPurposeRepository generalPurposeRepository) {
		long start = System.currentTimeMillis();
		generalPurposeRepository.save(new GeneralPurpose("Food"));
		generalPurposeRepository.save(new GeneralPurpose("Car"));
		generalPurposeRepository.save(new GeneralPurpose("Gas"));
		generalPurposeRepository.save(new GeneralPurpose("Beer"));
		generalPurposeRepository.save(new GeneralPurpose("Bills"));
		generalPurposeRepository.save(new GeneralPurpose("Internet"));
		generalPurposeRepository.save(new GeneralPurpose("Travel"));
		generalPurposeRepository.save(new GeneralPurpose("Vodka"));
		generalPurposeRepository.save(new GeneralPurpose("Transportation"));
		generalPurposeRepository.save(new GeneralPurpose("Pubs"));
		System.out.println(String.format("GeneralPurposes loaded in %dms", System.currentTimeMillis()-start));
	}


	public void insertAuthorities(AuthorityRepository authorityRepository) {
		long start = System.currentTimeMillis();
		Authority authorityAdmin = new Authority();
		Authority authorityUser = new Authority();
		authorityAdmin.setName(AuthorityName.ROLE_ADMIN);
		authorityUser.setName(AuthorityName.ROLE_USER);
		authorityRepository.save(authorityAdmin);
		authorityRepository.save(authorityUser);
		System.out.println(String.format("Authorities loaded in %dms", System.currentTimeMillis()-start));
	}

	public void insertUsers(UserRepository userRepository, LanguageRepository languageRepository, CurrencyRepository currencyRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, int count) {
		long start = System.currentTimeMillis();
		String [] lines = fileReader(NAMES_DATABASE);
		Set<String> set = new HashSet<>();
		Language language = languageRepository.findOne("ENG");
		Currency currency = currencyRepository.findOne("USD");
		String pass = passwordEncoder.encode("123456");
		List <Authority> authorities = new ArrayList<>();
		authorities.add(authorityRepository.findOne(Long.parseLong("2")));
		for(String str : lines)
			set.add(str);
		String[] db = set.toArray(new String[set.size()]);
		System.out.println(String.format("Users data prepared in %dms", System.currentTimeMillis()-start));
		for(int i = 0; i < count; i++){
			String [] details = db[i].split("	");
			User user = new User(details[0].toLowerCase() + "." + details[1].toLowerCase() + "@domain.com", pass, details[0] + " " + details[1]);
			user.setLanguage(language);
			user.setCurrency(currency);
			user.setAuthorities(authorities);
			userRepository.save(user);
			System.out.println(String.format("User ID %d saved", user.getId()));
		}
		System.out.println(String.format("Users loaded in %dms", System.currentTimeMillis()-start));
	}

	public void insertRecords(UserRepository userRepository, RecordRepository recordRepository, CustomPurposeRepository customPurposeRepository, int recordsPerDay, int days) {
		long start = System.currentTimeMillis();
		Iterable <User> users = userRepository.findAll();
		long date;
		System.out.println(String.format("Records data prepared in %dms", System.currentTimeMillis()-start));
		for (User u : users){
			System.out.println("Loading records for User ID: " + u.getId());
			List <GeneralPurpose> purposes = customPurposeRepository.findAllUserSpecified(u.getId());
			date = System.currentTimeMillis();
			for(int i = 0; i < days; i++){
				for(int j = 0; j < recordsPerDay; j++){
					Record record = new Record(u, getRandomPurpose(purposes), getRandomAmount(0.5, 199.9), date);
					recordRepository.save(record);
				}
				date = date - TimeUnit.DAYS.toMillis(1);
			}
		}
		System.out.println(String.format("Records loaded in %dms", System.currentTimeMillis()-start));
	}

	public void insertCustomPurposes(UserRepository userRepository, GeneralPurposeRepository generalPurposeRepository, CustomPurposeRepository customPurposeRepository) {
		long start = System.currentTimeMillis();
		Iterable <User> users = userRepository.findAll();
		//List <GeneralPurpose> purposes = generalPurposeRepository.findAll();
		for (User u : users){
			for(int i = 0; i < 3; i++){
				customPurposeRepository.save(new CustomPurpose(u, u.getName() + " " + (i+1)));
			}
		}
		System.out.println(String.format("CustomPurposes loaded in %dms", System.currentTimeMillis()-start));
	}

	public void insertChartSelections(ChartSelectionRepository chartSelectionRepository){
		long start = System.currentTimeMillis();
		ChartSelection selection1 = new ChartSelection(ChartSelectionId.CURRENT_MONTH_AVG, "Current Month Average", ChartType.PIECHART, false, false);
		ChartSelection selection2 = new ChartSelection(ChartSelectionId.PREV_MONTH_AVG, "Previous Month Average", ChartType.PIECHART, false, false);
		ChartSelection selection3 = new ChartSelection(ChartSelectionId.PREV_THREE_MONTH_AVG, "Previous 3 Month Average", ChartType.PIECHART, false, false);
		ChartSelection selection4 = new ChartSelection(ChartSelectionId.CUSTOM_PERIOD_AVG, "Custom Period Average", ChartType.PIECHART, true, false);
		ChartSelection selection5 = new ChartSelection(ChartSelectionId.LAST_THREE_MONTH_AVG_DETAILED, "Last 3 Month Average Detailed", ChartType.COLUMNCHART, false, false);
		ChartSelection selection6 = new ChartSelection(ChartSelectionId.LAST_SIX_MONTH_AVG_DETAILED, "Last 6 Month Average Detailed", ChartType.COLUMNCHART, false, false);
		ChartSelection selection7 = new ChartSelection(ChartSelectionId.LAST_YEAR_AVG_DETAILED, "Last Year Average Detailed", ChartType.COLUMNCHART, false, false);
		ChartSelection selection8 = new ChartSelection(ChartSelectionId.CUSTOM_PERIOD_AVG_DETAILED, "Custom Periode Average Detailed", ChartType.COLUMNCHART, true, false);
		chartSelectionRepository.save(selection1);
		chartSelectionRepository.save(selection2);
		chartSelectionRepository.save(selection3);
		chartSelectionRepository.save(selection4);
		chartSelectionRepository.save(selection5);
		chartSelectionRepository.save(selection6);
		chartSelectionRepository.save(selection7);
		chartSelectionRepository.save(selection8);
		ChartSelection selection9 = new ChartSelection(ChartSelectionId.USER_CURRENT_MONTH, "Current Month", ChartType.PIECHART, false, true);
		ChartSelection selection10 = new ChartSelection(ChartSelectionId.USER_PREV_MONTH, "Previous Month", ChartType.PIECHART, false, true);
		ChartSelection selection11 = new ChartSelection(ChartSelectionId.USER_PREV_THREE_MONTH_AVG, "Previous 3 Month Average", ChartType.PIECHART, false, true);
		ChartSelection selection12 = new ChartSelection(ChartSelectionId.USER_PREV_THREE_MONTH_TOTAL, "Previous 3 Month Total", ChartType.PIECHART, false, true);
		ChartSelection selection13 = new ChartSelection(ChartSelectionId.USER_CUSTOM_PERIOD_AVG, "Custom Period Average", ChartType.PIECHART, true, true);
		ChartSelection selection14 = new ChartSelection(ChartSelectionId.USER_CUSTOM_PERIOD_TOTAL, "Custom Period Total", ChartType.PIECHART, true, true);
		ChartSelection selection15 = new ChartSelection(ChartSelectionId.USER_LAST_THREE_MONTH_DETAILED, "Last 3 Month Detailed", ChartType.COLUMNCHART, false, true);
		ChartSelection selection16 = new ChartSelection(ChartSelectionId.USER_LAST_SIX_MONTH_DETAILED, "Last 6 Month Detailed", ChartType.COLUMNCHART, false, true);
		ChartSelection selection17 = new ChartSelection(ChartSelectionId.USER_LAST_YEAR_DETAILED, "Last Year Average Detailed", ChartType.COLUMNCHART, false, true);
		ChartSelection selection18 = new ChartSelection(ChartSelectionId.USER_CUSTOM_PERIOD_DETAILED, "Custom Periode Average Detailed", ChartType.COLUMNCHART, true, true);
		chartSelectionRepository.save(selection9);
		chartSelectionRepository.save(selection10);
		chartSelectionRepository.save(selection11);
		chartSelectionRepository.save(selection12);
		chartSelectionRepository.save(selection13);
		chartSelectionRepository.save(selection14);
		chartSelectionRepository.save(selection15);
		chartSelectionRepository.save(selection16);
		chartSelectionRepository.save(selection17);
		chartSelectionRepository.save(selection18);
		System.out.println(String.format("ChartSelections loaded in %dms", System.currentTimeMillis()-start));
	}

	private String [] fileReader(String pathToFile){
		try {
			FileReader fileReader;
			BufferedReader br;
			ArrayList<String> lines = new ArrayList<>();
			fileReader = new FileReader(new File(pathToFile));
			br = new BufferedReader(fileReader);
			String line = null;
			while ((line = br.readLine()) != null){
					lines.add(line);
			}br.close();
			String [] res = new String [lines.size()];
			lines.toArray(res);
			return res;
		} catch (IOException e) {
			return null;
		}
	}

	private GeneralPurpose getRandomPurpose(List <GeneralPurpose> db) {
//		ArrayList <GeneralPurpose> db = new ArrayList<>();
//		for(GeneralPurpose p : pps)
//			db.add(p);
		return db.get(getRandomInteger(0, db.size()));
	}

	private int getRandomInteger(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	private double getRandomAmount(double min, double max) {
//		double d = ThreadLocalRandom.current().nextDouble(min, max);
//		BigDecimal bd = new BigDecimal(d).setScale(2, RoundingMode.HALF_EVEN);
//		d = bd.doubleValue();
//		return d;
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
}
