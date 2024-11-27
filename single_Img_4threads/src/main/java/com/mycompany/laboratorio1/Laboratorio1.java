package com.mycompany.laboratorio1;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/* @author gamur */
public class Laboratorio1 {

    public static void main(String[] args) {
         try {
            // Cargar la imagen
            File archivo = new File("C:\\Users\\gamur\\OneDrive - UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE\\ESPE IV NIVEL SII2024\\COMPUTACIÓN PARALELA\\U1\\Lab1\\SISD_threads\\src\\main\\java\\com\\mycompany\\laboratorio1\\img1.jpg");
            BufferedImage imagen = ImageIO.read(archivo);

            int altura = imagen.getHeight();
            int ancho = imagen.getWidth();

            System.out.println("Procesando imagen de " + ancho + "x" + altura);

            // Crear y asignar hilos
            int numeroHilos = 4; // Dividir en 4 partes
            Thread[] hilos = new Thread[numeroHilos];

            int filasPorHilo = altura / numeroHilos;
            int finFila;
            
            long inicio = System.nanoTime(); // Registrar tiempo inicial
            
            for (int i = 0; i < numeroHilos; i++) {
                int inicioFila = i * filasPorHilo;
                
                if(i == numeroHilos - 1){
                    finFila = altura;
                }else{
                    finFila = inicioFila + filasPorHilo;
                }

                hilos[i] = new Thread(new ImageProcesorBW(imagen, inicioFila, finFila));
                hilos[i].start();
            }

            // Esperar a que todos los hilos terminen
            for (Thread hilo : hilos) {
                hilo.join();
            }

            // Guardar la nueva imagen
            File archivoSalida = new File("imagen_gris_conc.jpg");
            ImageIO.write(imagen, "jpg", archivoSalida);
            
            long fin = System.nanoTime(); // Registrar tiempo final

            System.out.println("Imagen procesada y guardada como 'imagen_gris_conc.png'");
            System.out.println("Tiempo de ejecución: " + (fin - inicio) / 1_000_000 + " ms");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
