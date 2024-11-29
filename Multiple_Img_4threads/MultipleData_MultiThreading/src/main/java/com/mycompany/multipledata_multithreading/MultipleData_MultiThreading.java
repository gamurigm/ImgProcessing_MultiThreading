package com.mycompany.multipledata_multithreading;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * @author gamur
 */
public class MultipleData_MultiThreading {

    public static void main(String[] args) {
        try {
            // Ruta de la carpeta que contiene las imágenes originales
            String rutaCarpetaOriginal = "C:\\Users\\gamur\\OneDrive - UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE\\ESPE IV NIVEL SII2024\\COMPUTACIÓN PARALELA\\U1\\Lab1\\Imagenes";
            File carpetaOriginal = new File(rutaCarpetaOriginal);

            // Ruta de la carpeta donde se guardarán las imágenes procesadas
            String rutaCarpetaProcesadas = "C:\\Users\\gamur\\OneDrive - UNIVERSIDAD DE LAS FUERZAS ARMADAS ESPE\\ESPE IV NIVEL SII2024\\COMPUTACIÓN PARALELA\\U1\\Lab1\\Multiple_Img_4threads\\MultipleData_MultiThreading\\img_procesadas\\";
            File carpetaProcesadas = new File(rutaCarpetaProcesadas);

            // Crear la carpeta de salida si no existe
            if (!carpetaProcesadas.exists()) {
                carpetaProcesadas.mkdirs();
            }

            // Crear una lista de archivos de imagen
            List<File> archivosImagen = new ArrayList<>();
            for (File archivo : carpetaOriginal.listFiles()) {
                if (archivo.isFile() && (archivo.getName().endsWith(".jpg") || archivo.getName().endsWith(".png"))) {
                    archivosImagen.add(archivo);
                }
            }

            if (archivosImagen.isEmpty()) {
                System.out.println("No se encontraron imágenes en la carpeta especificada.");
                return;
            }

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

            for (int idx = 0; idx < imagenes.size(); idx++) {
                BufferedImage imagen = imagenes.get(idx);
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

                // Guardar la nueva imagen en la carpeta de procesadas
                String nombreArchivoOriginal = archivosImagen.get(idx).getName();
                String nombreArchivoSalida = carpetaProcesadas.getAbsolutePath() + File.separator + "gris_" + nombreArchivoOriginal;
                File archivoSalida = new File(nombreArchivoSalida);
                ImageIO.write(imagen, "jpg", archivoSalida);
                System.out.println("Imagen procesada y guardada como " + archivoSalida.getAbsolutePath());
            }

            long fin = System.nanoTime(); // Registrar tiempo final
            System.out.println("Tiempo de ejecución total: " + (fin - inicio) / 1_000_000 + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
