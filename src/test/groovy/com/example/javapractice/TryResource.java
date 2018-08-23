package com.example.javapractice;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class TryResource {

    @Test
    public void testClosable() {
        ClassPathResource resource = new ClassPathResource("inputs/input");
        try ( Scanner scanner = new Scanner (resource.getFile()) ){
            while(scanner.hasNext()){
                System.out.print(scanner.nextLine());
            }
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}


