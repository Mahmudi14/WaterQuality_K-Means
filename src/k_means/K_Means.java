package k_means;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class K_Means {

    public static void main(String[] args) {
        double[][] dataBelumNormal = null;
        double[][] data = null;

        //Baca Data
        File file = new File("src/dataset/waterQuality.csv");
        ArrayList<double[]> dataArrayList = new ArrayList<>();

        Scanner sc;
        try {
            sc = new Scanner(file);
            sc.nextLine();//Skip line pertama
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] arrLine = line.split(",");
                double[] arrBarisDouble = new double[arrLine.length];
                boolean add = true;
                for (int i = 0; i < arrLine.length; i++) {
                    String isi = arrLine[i];
                    if (isi.equalsIgnoreCase("#NUM!")) {
                        add = false;
                        break;
                    }
                    arrBarisDouble[i] = Double.parseDouble(isi);
                }
                if (add) {
                    dataArrayList.add(arrBarisDouble);
                }
            }
        } catch (FileNotFoundException ex) {
        }

        dataBelumNormal = new double[dataArrayList.size()][];
        for (int i = 0; i < dataArrayList.size(); i++) {
            dataBelumNormal[i] = dataArrayList.get(i);
        }

        data = new double[dataBelumNormal.length][dataBelumNormal[0].length];
        //----------------------------------------------------------------------
        //Mencari Nilai MAX-MIN dari tiap kolom atribut
        //----------------------------------------------------------------------
        double[] minx = new double[dataBelumNormal[0].length];
        double[] maxx = new double[dataBelumNormal[0].length];
        for (int i = 0; i < dataBelumNormal[0].length; i++) {
            minx[i] = Double.MAX_VALUE;
            maxx[i] = Double.MIN_VALUE;
        }

        for (int i = 0; i < dataBelumNormal.length; i++) {
            for (int j = 0; j < dataBelumNormal[i].length; j++) {
                double value = dataBelumNormal[i][j];

                if (value < minx[j]) {
                    minx[j] = value;
                }
                if (value > maxx[j]) {
                    maxx[j] = value;
                }
            }
        }

        //----------------------------------------------------------------------
        //Proses Normalisasi MIN-MAX
        //----------------------------------------------------------------------
        data = new double[dataBelumNormal.length][dataBelumNormal[0].length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                double value = dataBelumNormal[i][j];
                double normalValue = (value - minx[j]) / (maxx[j] - minx[j]);
                data[i][j] = normalValue;
            }
        }

        int K = 2;

        ArrayList<Integer>[] result = new ArrayList[K];
        int[] cluster = new int[data.length];
        if (data != null) {
            double[][] centroid = new double[K][data[0].length];

            for (int i = 0; i < centroid.length; i++) {
                for (int j = 0; j < centroid[i].length; j++) {
                    centroid[i][j] = data[i][j];
                }
            }

            boolean adaBerubah = true;
            while (adaBerubah) {
                adaBerubah = false;
                double[][] distance = new double[data.length][K];

                for (int i = 0; i < distance.length; i++) {
                    int imin = -1;
                    double min = Double.MAX_VALUE;
                    for (int j = 0; j < K; j++) {
                        double d = 0;
                        for (int m = 0; m < centroid[j].length; m++) {
                            double xicj = data[i][m] - centroid[j][m];
                            double xicj2 = Math.pow(xicj, 2);
                            d += xicj2;
                        }
                        d = Math.sqrt(d);
                        if (d <= min) {
                            min = d;
                            imin = j;
                        }
                        distance[i][j] = d;
                    }
                    // cek apakah dia berubah 
                    if (cluster[i] != imin && imin >= 0) {
                        adaBerubah = true;
                        cluster[i] = imin;
                    }

                }//pencarian anggota 

                // hitung rata rata 
                double[][] sumCentroid = new double[centroid.length][centroid[0].length];
                double[] nMember = new double[K];
                for (int i = 0; i < cluster.length; i++) {
                    int c = cluster[i];
                    nMember[c]++;
                    for (int j = 0; j < sumCentroid[0].length; j++) {
                        sumCentroid[c][j] += data[i][j];
                    }
                }
                //hitung rata rata
                for (int i = 0; i < centroid.length; i++) {
                    for (int j = 0; j < centroid[i].length; j++) {
                        double rata_rata = sumCentroid[i][j] / nMember[i];
                        centroid[i][j] = rata_rata;
                    }
                }
            }
        }

        for (int i = 0; i < result.length; i++) {
            result[i] = new ArrayList<>();
        }
        for (int i = 0; i < cluster.length; i++) {
            int index = cluster[i];
            result[index].add(i);
        }

        for (int i = 0; i < result.length; i++) {
            System.out.println("====================================================================================");
            System.out.println("Kluster " + (i + 1));
            System.out.println("====================================================================================");
            for (int j = 0; j < result[i].size(); j++) {
                int index = result[i].get(j);
                System.out.println(Arrays.toString(data[index]));
            }
        }

        System.out.println("====================================================================================");
        for (int i = 0; i < result.length; i++) {
            System.out.println("Kluster " + (i + 1) + "= " + result[i].size());

        }
    }
}
