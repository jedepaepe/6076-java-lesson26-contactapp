package eu.epfc.j6077.contactapp;

import java.sql.*;
import java.util.Scanner;

public class Bootstrapper {
    private final static Scanner scanner = new Scanner(System.in);
    private final static String connectionString = "jdbc:h2:./contact";

    public static void main(String[] args) {
        initializeDb();
        String choose;
        do {
            System.out.println("Choisissez parmi les options suivantes");
            System.out.println("(1) lister les contacts");
            System.out.println("(2) ajouter un contact");
            System.out.println("(3) modifier un contact");
            System.out.println("(4) supprimer un contact");
            System.out.println("(Q) quitter l'application");
            choose = scanner.nextLine();
            switch (choose) {
                case "1" -> consultContacts();
                case "2" -> addContact();
                case "3" -> updateContact();
                case "4" -> deleteContact();
            }
        } while(! choose.equals("Q"));
    }

    private static void deleteContact() {
        try (Connection connection = DriverManager.getConnection(connectionString)){
            String sql = "delete CONTACT where id=?";
            System.out.print("identifiant: ");
            int id = Integer.parseInt(scanner.nextLine());
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateContact() {
        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "update CONTACT set FIRSTNAME=?, LASTNAME=?, EMAIL=?, PHONE=? where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            System.out.print("identifiant: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("prénom: ");
            String firstName = scanner.nextLine();
            System.out.print("nom de famille: ");
            String lastName = scanner.nextLine();
            System.out.print("email: ");
            String email = scanner.nextLine();
            System.out.print("téléphone: ");
            String phone = scanner.nextLine();
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt(5, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addContact() {
        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "insert into CONTACT (FIRSTNAME, LASTNAME, EMAIL, PHONE) values(?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            System.out.print("prénom: ");
            String firstName = scanner.nextLine();
            System.out.print("nom de famille: ");
            String lastName = scanner.nextLine();
            System.out.print("email: ");
            String email = scanner.nextLine();
            System.out.print("téléphone: ");
            String phone = scanner.nextLine();
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phone);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void consultContacts() {
        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "select ID, FIRSTNAME, LASTNAME, EMAIL, PHONE from CONTACT";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()) {
                int id = resultSet.getInt("ID");
                String firstName = resultSet.getString("FIRSTNAME");
                String lastName = resultSet.getString("LASTNAME");
                String email = resultSet.getString("EMAIL");
                String phone = resultSet.getString("PHONE");
                System.out.println(String.join(" - ", "" + id, firstName, lastName, email, phone));
//                System.out.println(id + " - " + firstName + " - " + lastName + " - " + email + " - " + phone);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeDb() {
        try (Connection connection = DriverManager.getConnection(connectionString)) {
            String sql = "create table if not exists CONTACT (" +
                    "ID integer primary key auto_increment," +
                    "FIRSTNAME varchar(32)," +
                    "LASTNAME varchar(32)," +
                    "EMAIL varchar(128)," +
                    "PHONE varchar(20)" +
                    ")";
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
