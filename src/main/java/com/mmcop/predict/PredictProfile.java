package com.mmcop.predict;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public class PredictProfile {

    public static void main(String[] args) {
        String input_file = "D:\\MASTER2016\\4.Cancer\\example2.txt";
        /*String[] input_files = {"miR_tumor_exp_blca.csv", "miR_tumor_exp_brca.csv", "miR_tumor_exp_chol.csv",
                "miR_tumor_exp_coad.csv", "miR_tumor_exp_esca.csv", "miR_tumor_exp_hnsc.csv", "miR_tumor_exp_kich.csv",
                "miR_tumor_exp_kirc.csv", "miR_tumor_exp_lihc.csv", "miR_tumor_exp_luad.csv", "miR_tumor_exp_prad.csv",
                "miR_tumor_exp_stad.csv", "miR_tumor_exp_thca.csv", "miR_tumor_exp_ucec.csv"};
        for (String input : input_files) {
            System.out.println(">>实验文件：" + input);
            new PredictProfile().predict("miRNA", "E:\\intellij\\MMCOP\\web\\", "D:\\MASTER2016\\4.Cancer\\" + input);
        }*/
        new PredictProfile().predict("DNAm", "E:\\intellij\\MMCOP\\web\\", input_file);
    }

    /*
    * @type: miRNA, DNAm
    * @root: server root path
    * @input: input file name including root path
    * */
    public LinkedHashMap<String, ArrayList<String>> predict(String type, String root, String input) {
        try {
            /*define result list*/
            LinkedHashMap<String, ArrayList<String>> result = new LinkedHashMap<>();

            /*define cancer type name list*/
            String[] name_list;
            int[] dimension_list;
            if (type.equals("miRNA")) {
                name_list = new String[]{"blca.miR", "brca.miR", "chol.miR", "coad.miR", "esca.miR", "hnsc.miR", "kich.miR",
                        "kirc.miR", "lihc.miR", "luad.miR", "prad.miR", "stad.miR", "thca.miR", "ucec.miR"};
                dimension_list = new int[]{9, 15, 4, 7, 11, 15, 12, 17, 17, 11, 6, 11, 11, 7};
            } else {
                name_list = new String[]{"blca.DNAm", "brca.DNAm", "coad.DNAm", "esca.DNAm", "hnsc.DNAm", "kirc.DNAm",
                        "kirp.DNAm", "lihc.DNAm", "lscc.DNAm", "luad.DNAm", "paad.DNAm", "prad.DNAm", "thca.DNAm", "ucec.DNAm"};
                dimension_list = new int[]{224, 142, 10, 547, 379, 99, 849, 157, 961, 101, 738, 84, 75, 265};
            }

            int model_num = name_list.length;
            String matrix[][] = new String[model_num][];
            ArrayList<String> instance = new ArrayList<>();
            for (int n=0; n<model_num; n++) {
                /*construct train set*/
                String name = name_list[n].toUpperCase();
                String train = root+type+"/train_arff/"+name+".train.arff";
                String test = root+type+"/test_arff/"+name+".test.arff";
                String clf_model = root+type+"/"+name+".model";
                String feature_model = root+type+"/feature_model.csv";

                /*generate test set*/
                int dimension = dimension_list[n];

                /*extract txt information about features (features_txt)*/
                BufferedReader bufferedReader = new BufferedReader(new FileReader(feature_model));
                String line;
                String[] features_txt = null;
                while(bufferedReader.ready()) {
                    line = bufferedReader.readLine();
                    if (Objects.equals(line.substring(0, line.indexOf(",")).toUpperCase(), name)) {
                        features_txt = line.substring(line.indexOf(",")+1).split(",");
                        break;
                    }
                }
                bufferedReader.close();

                /*turn txt information to number list (features_num)*/
                ArrayList<Integer> features_num = new ArrayList<>();
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(input));
                line = bufferedReader2.readLine().replace("\"", "").replace("\n", "");
                String[] features_head = line.substring(line.indexOf(",")+1).split(",");
                assert features_txt != null;
                for (String features : features_txt) {
                    for (int j = 0; j < features_head.length; j++) {
                        if (Objects.equals(features, features_head[j])) {
                            features_num.add(j);
                            break;
                        }
                    }
                }

                /*continue to read the file, and use features_num to screen the file, generate arff file for weka*/
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(test));
                bufferedWriter.write("@relation relation_name\n");
                if (type.equals("miRNA")) {
                    for (int i=0; i<dimension; i++) {
                        bufferedWriter.write("@attribute " + (i + 1) + " real\n");
                    }
                } else {
                    for (int i=0; i<dimension; i++) {
                        bufferedWriter.write("@attribute Fea" + (i) + " numeric\n");
                    }
                }
                bufferedWriter.write("@attribute class {+1,-1}\n@data\n");

                while(bufferedReader2.ready()) {
                    line = bufferedReader2.readLine();
                    if (line.equals("")) {
                        continue;
                    }
                    if (n == 0) {
                        instance.add(line.substring(0, line.indexOf(",")).replace("\"", ""));
                    }
                    String[] group = line.substring(line.indexOf(",")+1).trim().split(",");
                    for (Integer num : features_num) {
                        String value = group[num];
                        if (value.contains("NA")) {
                            return null;
                        }
                        bufferedWriter.write(value + ",");
                    }
                    /*write label, if not, set positive label*/
                    bufferedWriter.write("+1\n");
                }
                bufferedReader2.close();
                bufferedWriter.close();

                /*if (features_num.size() != dimension) {
                    System.out.println("实际维度"+features_num.size());
                    System.out.println("维度"+dimension);
                }*/

                /*load train set and test set*/
                Instances train_set = new Instances(new BufferedReader(new FileReader(train)));
                Instances test_set = new Instances(new BufferedReader(new FileReader(test)));
                train_set.setClassIndex(train_set.numAttributes() - 1);
                test_set.setClassIndex(test_set.numAttributes() - 1);

                /*load model and evaluate model*/
                Classifier classifier = (Classifier) weka.core.SerializationHelper.read(clf_model);
                Evaluation evaluation = new Evaluation(train_set);
                evaluation.evaluateModel(classifier, test_set);

                /*generate result*/
                /*double correct_rate = 1-evaluation.errorRate();
                System.out.println(name+": "+correct_rate);*/
                /*System.out.println(evaluation.toSummaryString());
                System.out.println(evaluation.toMatrixString());*/

                matrix[n] = new String[instance.size()];
                for (int i=0; i<test_set.size(); i++) {
                    double predict = classifier.classifyInstance(test_set.instance(i));
                    if (predict == 0) {
                        matrix[n][i] = "1";
                    } else {
                        matrix[n][i] = "0";
                    }
                }

            }

            /*format result*/
            for (int j=0; j<matrix[0].length; j++) {
                ArrayList<String> predicted = new ArrayList<>();
                for (int i=0; i<matrix.length; i++) {
                    predicted.add(matrix[i][j]);
                }
                result.put(instance.get(j), predicted);
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
