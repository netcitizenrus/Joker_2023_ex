import org.ejml.data.DMatrixSparseCSC;
import org.ejml.data.DMatrixSparseTriplet;
import org.ejml.ops.DConvertMatrixStruct;
import org.ejml.simple.SimpleMatrix;
import org.ejml.sparse.csc.CommonOps_DSCC;

import java.net.URISyntaxException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws URISyntaxException {

//        int artistId = 14256;

        LinkedList<int[]> interactions = Utils.readInteractions();

        int users = (int) interactions.stream().map(row -> row[0]).distinct().count();
        int artists = (int) interactions.stream().map(row -> row[1]).distinct().count();

        System.out.println(users);
        System.out.println(artists);

//        DMatrixSparseTriplet work = new DMatrixSparseTriplet(artists + 1, users + 1, 1);

        // заполняем спарс триплеты
//        interactions.forEach(row -> work.addItem(row[1], row[0], row[2]));
//
//        DMatrixSparseCSC matrix = DConvertMatrixStruct.convert(work, (DMatrixSparseCSC) null);
//
//        DMatrixSparseCSC matrix_t = new DMatrixSparseCSC(matrix);
//        CommonOps_DSCC.transpose(matrix, matrix_t, null);
//
//        DMatrixSparseCSC result = CommonOps_DSCC.mult(matrix, matrix_t, null);
//
//        SimpleMatrix denseMatrix = SimpleMatrix.wrap(result);
//        double[] row = denseMatrix.getRow(artistId).toArray2()[0];

//        Map<Integer, String> artistMap = Utils.readArtists();
//
//        System.out.println(Utils.getTopKIndices(row, 10).stream().map(artistMap::get).toList());
    }

}