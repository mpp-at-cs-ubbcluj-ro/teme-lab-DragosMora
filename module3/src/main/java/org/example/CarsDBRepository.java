package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("Finding cars by manufacturer: {}", manufacturerN);
        List<Car> cars = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from cars where manufacturer = ?")) {
            statement.setString(1, manufacturerN);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");

                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB: " + ex);
        }

        logger.traceExit(cars);
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry("Finding cars between years: {} - {}", min, max);
        List<Car> cars = new ArrayList<>();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("select * from cars where year > ? and year < ?")) {
            statement.setInt(1, min);
            statement.setInt(2, max);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");

                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB: " + ex);
        }

        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("saving taks: {}", elem);

        Connection con = dbUtils.getConnection();
        try (PreparedStatement statement = con.prepareStatement("insert into cars (manufacturer, model, year) values(?, ?, ?)")) {
            statement.setString(1, elem.getManufacturer());
            statement.setString(2, elem.getModel());
            statement.setInt(3, elem.getYear());
            int result = statement.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB: " + ex);
        }

        logger.traceExit();
    }

    @Override
    public void update(Integer id, Car elem) {
        logger.traceEntry("Updating car with id: {}, new values: {}", id, elem);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement statement = con.prepareStatement("update cars set manufacturer = ?, model = ?, year = ? where id = ?")) {
            statement.setString(1, elem.getManufacturer());
            statement.setString(2, elem.getModel());
            statement.setInt(3, elem.getYear());
            statement.setInt(4, id);
            int result = statement.executeUpdate();
            logger.trace("Updated {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB: " + ex);
        }

        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry();

        Connection con = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement statement = con.prepareStatement("select * from cars")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String manufacturer = result.getString("manufacturer");
                    String model = result.getString("model");
                    int year = result.getInt("year");

                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.out.println("Error DB: " + ex);
        }

        logger.traceExit(cars);
        return cars;
    }
}