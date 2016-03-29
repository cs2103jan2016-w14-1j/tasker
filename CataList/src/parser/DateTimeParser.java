package parser;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class DateTimeParser {
	private static final String SYMBOL_EMPTY = "";
	
	protected static final String INVALID_DATE_MESSAGE = "Invalid date input";
	protected static final String INVALID_TIME_MESSAGE = "Invalid time input";
	
	public static ArrayList<String> parseDateTime(String userInput){
		ArrayList<String> dateTimeArgs = new ArrayList<String>();
		String[] splitInput = userInput.split(" ");
		
		if(hasDateFlag(userInput)){
			int dateIndex = searchForDateFlagsIndex(splitInput);
			String dateWord;
			try{
				dateWord = splitInput[dateIndex];
			} catch (IndexOutOfBoundsException e){
				dateWord = SYMBOL_EMPTY;
			}
			
			if(dateWord != null){
				dateTimeArgs.add(parseForDate(dateWord));
			} else {
				dateTimeArgs.add(INVALID_DATE_MESSAGE);
			}
		}
		
		if(hasTimeFlag(userInput)){
			int timeIndex = searchForTimeFlagsIndex(splitInput);
			String timeWord;
			try{
				 timeWord = splitInput[timeIndex];
			} catch (IndexOutOfBoundsException e){
				timeWord = SYMBOL_EMPTY;
			}
			
			if(timeWord != null){
				dateTimeArgs.add(parseForTime(timeWord));
			} else {
				dateTimeArgs.add(INVALID_TIME_MESSAGE);
			}
			if(!hasDateFlag(userInput)){
					dateTimeArgs.add(getToday());
			}
		} 
		return dateTimeArgs;
	}
	
	private static int searchForDateFlagsIndex(String[] splittedInput){
		int indexOfDate = 0;
		for(int i = 0; i <splittedInput.length; i++){
			for(String eachKeyword : KeywordConstraints.KW_DATE_FLAG){
				if(splittedInput[i].contains(eachKeyword)){
					indexOfDate = i;
					break;
				}
			}
		}
		return indexOfDate + 1;
	}
	
	private static int searchForTimeFlagsIndex(String[] splittedInput){
		int indexOfTime = 0;
		for(int i = 0 ; i<splittedInput.length; i++){
			for(String eachKeyword : KeywordConstraints.KW_TIME_FLAG){
				if(splittedInput[i].contains(eachKeyword)){
					indexOfTime = i;
					break;
				}
			}
		}
		return indexOfTime + 1;
	}
	private static String getToday(){
		DateTime now = new DateTime();
		LocalDate today = now.toLocalDate();
		String dateParsed = today.toString(KeywordConstraints.KW_FORMAT_DATE_STORAGE);
		return dateParsed;
	}
	private static String parseForDate(String targetString){
		LocalDate curDate = new LocalDate();
		String dateParsed = SYMBOL_EMPTY;
		
		if(!parseForDateWithYear(curDate, targetString).isEmpty()){
			dateParsed = parseForDateWithYear(curDate, targetString);
		} else if (!parseForDateNoYear(curDate, targetString).isEmpty()){
			dateParsed = parseForDateNoYear(curDate, targetString);
		}
		
		return dateParsed;

	}
	
	private static String parseForDateNoYear(LocalDate currentDate, String targetString) {
		LocalDate targetDate = null;
		String dateParsed = SYMBOL_EMPTY;
		
		for(String eachKeyword : KeywordConstraints.KW_FORMAT_DATE_WITHOUT_YEAR){
			try{
				targetDate = DateTimeFormat.forPattern(eachKeyword).parseLocalDate(targetString)
										.withYear(currentDate.getYear());
				dateParsed = targetDate.toString(KeywordConstraints.KW_FORMAT_DATE_STORAGE);
			} catch (IllegalArgumentException e) {
				//nothing 
			}
		}
		return dateParsed;
	}
	
	private static String parseForDateWithYear(LocalDate currentDate, String targetString) {
		LocalDate targetDate = null;
		String dateParsed = SYMBOL_EMPTY;
		
		for(String eachKeyword : KeywordConstraints.KW_FORMAT_DATE_WITH_YEAR){
			try{
				targetDate = DateTimeFormat.forPattern(eachKeyword).parseLocalDate(targetString);
				dateParsed = targetDate.toString(KeywordConstraints.KW_FORMAT_DATE_STORAGE);
			} catch (IllegalArgumentException e){
				//nothing.
			}
		}
		
		return dateParsed;
	}
	
	private static String parseForTime(String targetString){
		LocalTime curTime = null;
		String timeParsed = SYMBOL_EMPTY;
		for(String eachKeyword : KeywordConstraints.KW_FORMAT_TIME){
			try{
			curTime = DateTimeFormat.forPattern(eachKeyword).parseLocalTime(targetString);
			timeParsed = curTime.toString(KeywordConstraints.KW_FORMAT_TIME_STORAGE);
			} catch (IllegalArgumentException e){
				//nothing
			}
		}
		return timeParsed;
	}
	
	private static boolean hasDateFlag(String userInput){
		boolean result = false;
		for(String eachKeyword : KeywordConstraints.KW_DATE_FLAG){
			if(userInput.contains(eachKeyword)){
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	private static boolean hasTimeFlag(String userInput){
		boolean result = false;
		for(String eachKeyword : KeywordConstraints.KW_TIME_FLAG){
			if(userInput.contains(eachKeyword)){
				result = true;
				break;
			}
		}
		
		return result;
	}
}
