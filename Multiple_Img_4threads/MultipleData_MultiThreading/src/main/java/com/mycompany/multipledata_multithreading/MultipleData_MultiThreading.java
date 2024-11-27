package com.mycompany.multipledata_multithreading;

/* @author gamur*/

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;


/* @author gamur */
public class MultipleData_MultiThreading {

    public static void main(String[] args) {
        try {
            // Crear una lista de archivos de imagen
            List<File> archivosImagen = new ArrayList<>();
            archivosImagen.add(new File("C:\\Users\\gamur\\OneDrive - UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE\\ESPE IV NIVEL SII2024\\COMPUTACIÓN PARALELA\\U1\\Lab1\\Imagenes\\img1.jpg"));
            archivosImagen.add(new File("C:\\Users\\gamur\\OneDrive - UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE\\ESPE IV NIVEL SII2024\\COMPUTACIÓN PARALELA\\U1\\Lab1\\Imagenes\\img2.jpg"));
            // Agregar más imágenes según sea necesario

            // Lista para almacenar las imágenes procesadas
            List<BufferedImage> imagenes = new ArrayList<>();

            // Cargar todas las imágenes
            for (File archivo : archivosImagen) {
                BufferedImage imagen = ImageIO.read(archivo);
                imagenes.add(imagen);
            }

            // Crear y asignar hilos para cada imagen
            int numeroHilos = 4; // Dividir en 4 partes por imagen
            Thread[] hilos = new Thread[numeroHilos];

            long inicio = System.nanoTime(); // Registrar tiempo inicial

            for (BufferedImage imagen : imagenes) {
                int altura = imagen.getHeight();
                int ancho = imagen.getWidth();
                int filasPorHilo = altura / numeroHilos;

                // Procesar la imagen con hilos
                for (int i = 0; i < numeroHilos; i++) {
                    int inicioFila = i * filasPorHilo;
                    int finFila = (i == numeroHilos - 1) ? altura : inicioFila + filasPorHilo;

                    hilos[i] = new Thread(new ImageProcesorBW(imagen, inicioFila, finFila));
                    hilos[i].start();
                }

                // Esperar a que todos los hilos terminen
                for (Thread hilo : hilos) {
                    hilo.join();
                }

                // Guardar la nueva imagen
                String nombreArchivoSalida = "imagen_gris_" + System.currentTimeMillis() + ".jpg";
                File archivoSalida = new File(nombreArchivoSalida);
                ImageIO.write(imagen, "jpg", archivoSalida);
                System.out.println("Imagen procesada y guardada como " + nombreArchivoSalida);
            }

            long fin = System.nanoTime(); // Registrar tiempo final
            System.out.println("Tiempo de ejecución total: " + (fin - inicio) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
