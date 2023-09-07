import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.ops.DConvertMatrixStruct;
import org.ejml.simple.SimpleMatrix;
import org.ejml.sparse.csc.CommonOps_DSCC;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws URISyntaxException {

        Map<Integer, String> artistMap = readArtists();
        LinkedList<int[]> interactions = readInteractions();

        // в реальности не сможем так прочитать

        int users = (int) interactions.stream().map(row -> row[0]).distinct().count();
        int artists = (int) interactions.stream().map(row -> row[1]).distinct().count();

        System.out.println(users);
        System.out.println(artists);

        DMatrixSparseTriplet work = new DMatrixSparseTriplet(artists + 1, users + 1, 114562);

        interactions.forEach(row -> work.addItem(row[1], row[0], (double) row[2]));

        DMatrixSparseCSC matrix1 = new DMatrixSparseCSC(1, 2);
        DMatrixSparseCSC matrix = DConvertMatrixStruct.convert(work, matrix1);
        DMatrixSparseCSC matrix_t = new DMatrixSparseCSC(matrix);
        DMatrixSparseCSC result = new DMatrixSparseCSC(matrix);
        CommonOps_DSCC.transpose(matrix, matrix_t, null);

       // в реальности не можем такое умножать (искать похожих)
        CommonOps_DSCC.mult(matrix, matrix_t, result);

        SimpleMatrix simp = SimpleMatrix.wrap(result);
        double[] row = simp.getRow(4194).toArray2()[0];
        System.out.println(getTopKIndices(row, 15).stream().map(artistMap::get).toList());
    }
        static Map<Integer, String> readArtists() throws URISyntaxException {

            File test = new File(Objects.requireNonNull(Main.class.getClassLoader()
                    .getResource("lastfm_artist_list.csv")).toURI());

            String line;
            Map<Integer, String> artistMap = new HashMap<>();

            try (BufferedReader br = new BufferedReader(new FileReader(test))) {
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 2) {
                        Integer artistId = Integer.parseInt(data[0].trim());
                        String artistName = data[1].trim();
                        artistMap.put(artistId, artistName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return artistMap;
        }

        static List<Integer> getTopKIndices(double[] scores, int k) {
            Comparator<Map.Entry<Integer, Double>> comparator = Map.Entry.comparingByValue();
            PriorityQueue<Map.Entry<Integer, Double>> heap = new PriorityQueue<>(scores.length, comparator.reversed());

            for (int i = 0; i < scores.length; i++)
                heap.add(new AbstractMap.SimpleEntry<>(i, scores[i]));

            List<Integer> topKIndices = new LinkedList<>();
            for (int i = 0; i < k && !heap.isEmpty(); i++)
                topKIndices.add(heap.poll().getKey());

            return topKIndices;
        }

    static LinkedList<int[]> readInteractions() throws URISyntaxException {

        File test = new File(Objects.requireNonNull(Main.class.getClassLoader()
                .getResource("lastfm_user_scrobbles.csv")).toURI());

        String line;
        LinkedList<int []> scrobbles = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(test))) {
            while ((line = br.readLine()) != null) {
                int[] data = Arrays.stream(line.split(","))
                        .flatMapToInt(num -> IntStream.of(Integer.parseInt(num))).toArray();
                scrobbles.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scrobbles;
    }
}