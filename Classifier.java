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

    // currently k = 1
//    public String classify(short[][] pixels) {
//    String closestValue = "9"; //variable
//    double distance = Integer.MAX_VALUE; //variable
//        for (DataPoint dp : trainingData) {
//        if (distance(pixels, dp.getData().getBWPixelGrid()) < distance) {
//            distance = distance(pixels, dp.getData().getBWPixelGrid());
//            closestValue = dp.getLabel();
//        }
//    }
//
//        return closestValue;
//}

    //goal k = 2
    public String classify(short[][] pixels) {
        if (trainingData.size() == 0) return "no training data";

        ArrayList<String> closestLabels = new ArrayList<>();

        DataPoint currentClosest = new DataPoint();
        for (int i = 0; i < n; i++) {
            currentClosest = findClosest(pixels);
            closestLabels.add(currentClosest.getLabel());
            trainingData.remove(currentClosest);
        }

        return mode(closestLabels);
    }

    private DataPoint findClosest(short[][] pixels) {
        double distance = Integer.MAX_VALUE; //variable
        DataPoint closest = new DataPoint();
        for (DataPoint dp : trainingData) {
            if (distance(pixels, dp.getData().getBWPixelGrid()) < distance) {
                distance = distance(pixels, dp.getData().getBWPixelGrid());
                closest = dp;
            }
        }
        return closest;
    }

    //assuming contains compares string values, not object heap locations
    private String mode(ArrayList<String> arr) {
        ArrayList<String> uniqueCharacters = new ArrayList<>();
        for (String str : arr) {
            if (!contains(arr, str)) {
                uniqueCharacters.add(str);
            }
        }
        System.out.println(uniqueCharacters);
        int[] repetitions = new int[uniqueCharacters.size()];
        for (String str : arr) {
            repetitions[uniqueCharacters.indexOf(str)]++;
        }
        int maxIndex = 0;
        for (int i = 0; i < repetitions.length; i++) {
            if (repetitions[i] > repetitions[maxIndex]) {
                maxIndex = i;
            }
        }
        return uniqueCharacters.get(maxIndex);
    }

    private boolean contains(ArrayList<String> arr, String str) {
        for (String string : arr) {
            if (arr.equals(str)) return true;
        }
        return false;
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

        return sum;
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
