package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.*;

public class MainBD {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("/Users/dragosmora1/Documents/UBB_INFO/MPP/module3/bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        CarRepository carRepo=new CarsDBRepository(props);
        carRepo.add(new Car("Tesla","Model S", 2019));
        System.out.println("Toate masinile din db");
        for(Car car:carRepo.findAll())
            System.out.println(car);
       String manufacturer="Tesla";
        System.out.println("Masinile produse de "+manufacturer);
        for(Car car:carRepo.findByManufacturer(manufacturer))
            System.out.println(car);

        carRepo.add(new Car("Ford", "Mustang", 1969));
        carRepo.update(1, new Car("Tesla", "Model X", 2022));
        StreamSupport.stream(carRepo.findAll().spliterator(), false)
                .filter(car -> Objects.equals(car.getYear(), 2022))
                .forEach(System.out::println);

    }
}
