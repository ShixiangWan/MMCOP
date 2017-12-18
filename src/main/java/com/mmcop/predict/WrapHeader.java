package com.mmcop.predict;

import java.util.ArrayList;

public class WrapHeader {
    /*generate true name compared with miRNA or DNAm*/
    public ArrayList<String> wrap(String type) {
        ArrayList<String> header = new ArrayList<>();
        if (type.equals("miRNA")) {
            header.add("Bladder");
            header.add("Breast");
            header.add("Bile Duct");
            header.add("Colorectal");
            header.add("Esophagus");
            header.add("Head and Neck");
            header.add("Kidney Chromophobe");
            header.add("Kidney Renal Clear Cell");
            header.add("Liver");
            header.add("Lung");
            header.add("Prostate");
            header.add("Stomach");
            header.add("Thyroid");
            header.add("Uterus");
        } else {
            header.add("Bladder");
            header.add("Breast");
            header.add("Colorectal");
            header.add("Esophagus");
            header.add("Head and Neck");
            header.add("Kidney Renal Clear Cell");
            header.add("Kidney Renal Papillary Cell");
            header.add("Liver");
            header.add("Lingual squamous cell");
            header.add("Lung");
            header.add("Pancreas");
            header.add("Prostate");
            header.add("Thyroid");
            header.add("Uterus");
        }
        return header;
    }
}
