package com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MainClass {

	static Scanner input = new Scanner(System.in);


	private static final Map<Integer, String[]> trainList;
	static String[] train_12101 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static String[] train_12102 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static String[] train_12103 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static String[] train_12104 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static String[] train_12105 = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
	static
	{
		trainList = new HashMap<>();
		trainList.put(1, train_12101);
		trainList.put(2, train_12102);
		trainList.put(3, train_12103);
		trainList.put(4, train_12104);
		trainList.put(5, train_12105);
	}

	private static final Map<Integer, Integer> trainNoList;
	static
	{
		trainNoList = new HashMap<>();
		trainNoList.put(1, 12101);
		trainNoList.put(2, 12102);
		trainNoList.put(3, 12103);
		trainNoList.put(4, 12104);
		trainNoList.put(5, 12105);
	}


	static Map<Map<String,String>, Map<String,String>> bookedMap = new HashMap<>();

	static String menu = "C:\\Users\\alan1\\Desktop\\menu.txt";
	static String availableTrains = "C:\\Users\\alan1\\Desktop\\availableTrains.txt";
	static String bookingHistory = "C:\\Users\\alan1\\Desktop\\BookedHistory.txt";

	static String ref_Id = null;

	/**
	 * Method to read a file for all options
	 */
	public static void fileReader(String fileName)
	{

		File file = new File(fileName);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, Charset.defaultCharset());
			BufferedReader br = new BufferedReader(isr);
			String line;
			while((line = br.readLine()) != null){
				//process the line
				System.out.println(line);
			}
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String ran() {

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 6;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) 
		{
			int randomLimitedInt = leftLimit + (int) 
					(random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String generatedString = buffer.toString();
		return generatedString;

	}

	public static String bookedHistory(String seatNumber, String Name, String email, String selectedTrain)
	{
		String bookingId = ran();
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter(bookingHistory, true));
			out.write(selectedTrain+"	"+seatNumber+"	"+bookingId+"	"+Name+"	"+email);
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bookingId;
	}

	public static Boolean bookSeat(String seatNumber, String[] train_No) throws IOException
	{
		for(int i = 0; i < train_No.length; i++)
		{
			if(train_No[i].equals(seatNumber))
			{
				train_No[i] = "Booked";
				return true;
			}
		}
		return false;
	}

	public static void showTrains()
	{
		System.out.println("\nHere are the available Trains! Loading..");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		fileReader(availableTrains);
	}

	public static void showMenu()
	{
		System.out.println("Welcome to the train reservation system!");
		fileReader(menu);
		System.out.println("\nEnter your choice : ");
	}

	public static void bookTrain() throws IOException
	{
		showTrains();
		System.out.println("Enter train number");
		int trainNo = input.nextInt();
		String selectedTrain = Files.readAllLines(Paths.get(availableTrains)).get(trainNo - 1);
		System.out.println("SelectedTrain :"+selectedTrain+"\nAvailable Seats"+ Arrays.toString(trainList.get(trainNo)));

		System.out.println("Enter your Seat Number");
		int seatNumber = input.nextInt();
		System.out.println("Enter Name:");
		String name = input.next();
		System.out.println("Enter Email:");
		String email = input.next();
		System.out.println("Confirm? y or n");
		char c = input.next().charAt(0);
		if('y'== c)
		{
			if(bookSeat(String.valueOf(seatNumber), trainList.get(trainNo)))
			{
				String id = bookedHistory(String.valueOf(seatNumber), name, email, selectedTrain);
				ref_Id = id;
				System.out.println("Your Seat "+seatNumber+" is booked and your booking id is "+id);
				Map<String, String> key = new HashMap<>();
				key.put(id, email);

				Map<String, String> value = new HashMap<>();
				value.put(String.valueOf(seatNumber), String.valueOf(trainNo));

				bookedMap.put(key, value);
			}
			else
			{
				System.out.println("Seat is not avaiable or already booked. \n Restarting the booking process");
				bookTrain();
			}
		}
		else
		{
			System.out.println("Retry from Menu");
		}
	}

	public static void bookingStatus(String bookingId, String emailId)
	{
		Map<String, String> statusMap = new HashMap<>();
		statusMap.put(bookingId, emailId);
		Map<String, String> resultMap = bookedMap.get(statusMap);
		if(resultMap != null)
		{
			for ( Map.Entry<String, String> entry : resultMap.entrySet()) 
			{
				String seatNo = entry.getKey();
				String trainNumber = entry.getValue();

				if(trainList.get(Integer.parseInt(trainNumber))[Integer.parseInt(seatNo) - 1].equals("Booked"))
				{
					trainList.get(Integer.parseInt(trainNumber))[Integer.parseInt(seatNo) - 1] = seatNo;
					bookedMap.remove(statusMap);
					System.out.println("Cancelled");
					break;
				}
			}
		}
		else
		{
			System.out.println("Not valid!");
		}

	}

	public static Boolean findBookingStatus(String bookingId, String emailId) throws IOException
	{
		File file = new File(bookingHistory);
		Boolean found = false;
		try {
			Scanner scanner = new Scanner(file);
			int lineNum = 0;
			while (scanner.hasNextLine()) 
			{
				String line = scanner.nextLine();
				if(line.contains(bookingId) && line.contains(emailId)) 
				{
					String a = Files.readAllLines(Paths.get(bookingHistory)).get(lineNum);
					System.out.println(a);
					return true;
				}
				lineNum++;
			}
		} catch(FileNotFoundException e) { 
			System.out.println("error");
		}
		return found;
	}


	public static void main(String[] args) throws IOException, InterruptedException 
	{

		showMenu();
		int number = input.nextInt();
		String line = Files.readAllLines(Paths.get(menu)).get(number-1);
		System.out.println("You have selected "+line);		
		switch(number)
		{
		case 1 :
			showTrains();
			break;
		case 2 :
			bookTrain();
			break;
		case 3:
			System.out.print("You are about to cancel you booked ticket\nEnter your booking Id ");
			String bookedId = input.next();
			System.out.println("Enter your emailId");
			String email = input.next();
			if(findBookingStatus(bookedId, email))
			{
				System.out.println("Confirm? y or n");
				char c = input.next().charAt(0);
				if('y'== c)
				{
					bookingStatus(bookedId, email);
				}
				else
				{
					System.out.println("Booking cancelled");
				}	
			}

			break;
		case 4:
			System.out.println("Booking Status\nEnter your booking Id ");
			String bookingId = input.next();
			System.out.println("Enter your emailId");
			String emailId = input.next();
			if(!findBookingStatus(bookingId, emailId))
			{
				System.out.println("Not Valid");
			}
			break;
		case 5:
			for ( Map.Entry<Integer, String[]> entry : trainList.entrySet()) 
			{
				System.out.println("Train number : "+ trainNoList.get(entry.getKey())+ " Seats "+Arrays.toString(entry.getValue()));
			}
			break;

		}

		System.out.println("\n\n");
		main(args);
	}

}
