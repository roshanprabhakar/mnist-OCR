import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Classifier {
    private ArrayList<DataPoint> trainingData;
    private int n;

    public Classifier(int n) {
        this.n = n;
        trainingData = new ArrayList<DataPoint>();
    }

    public void addTrainingData(List<DataPoint> points) {
        for (DataPoint p : points) addTrainingData(p);
    }

    public void addTrainingData(DataPoint point) {
        trainingData.add(point);
    }

    public void addTrainingData(String label, DImage img) {
        addTrainingData(new DataPoint(label, img));
    }

    public String classify(short[][] pixels) {
        if (trainingData.size() == 0) return "no training data";

        String closestValue = "9"; //variable
        double distance = Integer.MAX_VALUE; //variable
        for (DataPoint dp : trainingData) {
            if (distance(pixels, dp.getData().getBWPixelGrid()) < distance) {
                distance = distance(pixels, dp.getData().getBWPixelGrid());
                closestValue = dp.getLabel();
            }
        }

        return closestValue;
    }

    public String classify(DImage img) {
        return classify(img.getBWPixelGrid());
    }

    public double distance(short[][] d1, short[][] d2) {
        double sum = 0;

        for (int r = 0; r < d1.length; r++) {
            for (int c = 0; c < d1[r].length; c++) {
                sum += (d2[r][c] - d1[r][c]) * (d2[r][c] - d1[r][c]);
            }
        }

        return Math.sqrt(sum);
    }

    public void test(List<DataPoint> test) {
        ArrayList<DataPoint> correct = new ArrayList<>();
        ArrayList<DataPoint> wrong = new ArrayList<>();

        int i = 0;
        for (DataPoint p : test) {
            String predict = classify(p.getData());
            System.out.print("#" + i + " REAL:\t" + p.getLabel() + " predicted:\t" + predict);
            if (predict.equals(p.getLabel())) {
                correct.add(p);
                System.out.print(" Correct ");
            } else {
                wrong.add(p);
                System.out.print(" WRONG ");
            }

            i++;
            System.out.println(" % correct: " + ((double) correct.size() / i));
        }

        System.out.println(correct.size() + " correct out of " + test.size());
        System.out.println(wrong.size() + " correct out of " + test.size());
        System.out.println("% Error: " + (double) wrong.size() / test.size());
    }
}
