/**
 * Created by Ssangwook on 12/15/2015.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.bounce.FormLayout;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.functions.*;
import weka.classifiers.functions.supportVector.CachedKernel;
import weka.classifiers.functions.supportVector.CheckKernel;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.PolyKernel;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.lazy.LWL;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.*;
import weka.filters.Filter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Predict_Billboard {
    String pathTrain;
    String pathTest;
    String output="";
    /*
        input: a txt file formatted. (Please see the sample input for the format.)
        output: the inputreader.
     */
    public static BufferedReader readData(String filename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }
        return reader;
    }

    /*
        input: our model, training set, testing set
        output: evaluation of our model.
     */
    public static Evaluation classify(Classifier model,Instances trainS, Instances testS) throws Exception {
        Evaluation evaluation = new Evaluation(trainS);
        // we build our model here.
        model.buildClassifier(trainS);
        // we evaluate our model on the testingset.
        evaluation.evaluateModel(model, testS);
        return evaluation;
    }

    public static double accuracyIs(FastVector pred) {
        double correct = 0;
        for (int i = 0; i < pred.size(); i++) {
            NominalPrediction np = (NominalPrediction) pred.elementAt(i);
            if (np.predicted() == np.actual()) {
                correct++;
            }
        }
        return 100 * correct / pred.size();
    }

    public static String printOutResult(FastVector pred, String[] artist, String[] title, String output) {
        double correct = 0;
        output+= ("ARTIST   TITLE   ACTUAL MOVEMENT    PREDICTED MOVEMENT\n==================================\n\n");
        int count = 0;
        for (int i = 0; i < pred.size(); i++) {
            NominalPrediction np = (NominalPrediction) pred.elementAt(i);
            double pre = np.predicted() - 1;
            double act = np.actual() - 1;
            output+=(""+artist[i]+"    "+title[i]+"    "+act+"   "+pre+"\n");
            if (act == pre) {
                correct++;
            }
            count++;
        }
        output+=("\nWe guessed " + correct + " movements out of " + count + " movements correctly.\nThat is " + (100 * correct / count) + "% accurate.");
        return output;
    }

    public static Instances[][] cvSplit(Instances dataset, int numFolds) {
        Instances[][] split = new Instances[2][numFolds];
        for (int i = 0; i < numFolds; i++) {
            split[0][i] = dataset.trainCV(numFolds, i);
            split[1][i] = dataset.testCV(numFolds, i);
        }
        return split;
    }


    public static void main(String[] args) throws Exception {
        //========================
        JFrame jFrame = new JFrame("Billboard Predictor Demo");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jFrame.setSize(600, 480);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);

        //Initialise Panel 1 & Components
        JPanel p1 = new JPanel(new GridBagLayout());
        JPanel p2 = new JPanel(new GridBagLayout());
        //Label 1 - For TextArea
        JLabel l1 = new JLabel("Output log");
        //TextArea - To display conversation
        final JTextArea t1 = new JTextArea(30, 70);
        JScrollPane pane = new JScrollPane(t1);
        //button3
        JButton b3 = new JButton("Open Training Data");
        final JTextField tf3 = new JTextField(30);
//        tf3.setMinimumSize(new Dimension(100,30));
        //buttong2
        JButton b2 = new JButton("Open Testing Data");
        final JTextField tf2 = new JTextField(30);
//        tf2.setMinimumSize(new Dimension(100,30));
        //Button 1 - To send message
        JButton b1 = new JButton("Run");

        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.gridx = 0;
        gc.gridy = 0;
        p1.add(l1, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        p1.add(pane, gc);

        GridBagConstraints gc2 = new GridBagConstraints();
        gc2.fill = GridBagConstraints.HORIZONTAL;
        gc2.weightx = 1;
        gc2.gridx = 0;
        gc2.gridy = 0;
        gc2.ipadx = 10;
        p2.add(tf3, gc2);

        gc2.gridx = 1;
        gc2.gridy = 0;
        p2.add(b3, gc2);

        gc2.gridx = 2;
        gc2.gridy = 0;
        p2.add(tf2, gc2);

        gc2.gridx = 3;
        gc2.gridy = 0;
        p2.add(b2, gc2);

        gc2.gridx = 3;
        gc2.gridy = 1;
        p2.add(b1, gc2);

        final Predict_Billboard demo = new Predict_Billboard();
        String str_pathTestingData = "./";
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    demo.pathTest = selectedFile.getAbsolutePath();
                    tf2.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        String str_pathTrainingData = "./";
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    demo.pathTrain = selectedFile.getAbsolutePath();
                    tf3.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try {
                    demo.runIt(demo.pathTrain, demo.pathTest);
                    t1.setText(demo.output);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

        jFrame.add(p1, BorderLayout.CENTER);
        jFrame.add(p2, BorderLayout.SOUTH);

        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }



    public void runIt(String pathTrainData, String PathTestData) throws Exception {
        WekaPackageManager.loadPackages(false, true, false);
        BufferedReader thisData = readData(this.pathTrain);
        Instances dataset = new Instances(thisData);
        //useFilter(data);
        dataset.setClassIndex(dataset.numAttributes() - 1);

        Instances[][] split = cvSplit(dataset, 10);
        Instances[] trainingS = split[0];
        Instances[] testingS = split[1];

        Classifier[] models = {
                new PART(),
                new Logistic(),
                new SMO(),
                new DecisionTable(),
                new DecisionStump(),
                new NaiveBayes(),
                new J48(),
                new RandomForest()
        };
        double maxAccuracy=0;
        Classifier maxAccuracyClf= new J48();

        for (int j = 0; j < models.length; j++) {

            FastVector predictions = new FastVector();

            for (int i = 0; i < trainingS.length; i++) {
                Evaluation validation = classify(models[j], trainingS[i], testingS[i]);
                predictions.appendElements(validation.predictions());
            }

            double accuracy = accuracyIs(predictions);

            output+=("The Accuracy of the model," + models[j].getClass().getSimpleName() + ": "
                    + String.format("%.2f%%", accuracy)
                    + "\n---------------------------------\n");
            // pick the highest accuracy model
            if (accuracy>maxAccuracy) {
                maxAccuracyClf=models[j];
                maxAccuracy=accuracy;
            }
        }
        output+=("Our highest accuracy classifier is "+ maxAccuracyClf.getClass().getSimpleName()+".\n\n");
//        BufferedReader testData = readDataFile("C:/PA_FINAL/DATA_TEST_STR.txt");
        BufferedReader testData = readData(this.pathTest);
        Instances testDataset = new Instances(testData);
        testDataset.setClassIndex(testDataset.numAttributes()-1);
        String[] artist = new String[testDataset.numInstances()];
        String[] title = new String[testDataset.numInstances()];
        for (int i = 0; i < testDataset.numInstances(); i++)
        {
            artist[i]=testDataset.instance(i).stringValue(0);
            title[i]=testDataset.instance(i).stringValue(1);
        }
        testDataset.deleteStringAttributes();


        Evaluation validation2 = classify(maxAccuracyClf,dataset,testDataset);
        FastVector pred = new FastVector();
        pred.appendElements(validation2.predictions());
        output = printOutResult(pred, artist, title, output);


    }

}
