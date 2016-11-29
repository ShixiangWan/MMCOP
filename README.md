# MMCOP
根据miRNA或DNAm预测癌症。访问地址为：http://server.malab.cn/MMCOP/index.jsp

###1. 开发环境

* Operate System: Windows 8.1
* IDE: Intellij IDEA
* Server: Tomcat 9
* Language: Java 8
* MVC: Spring, Spring MVC
* Front: Bootstrap

###2. 功能介绍

* 预测RNA序列位置：用户上传fasta格式的RNA序列后，点击`Predict`按钮，稍等片刻，页面将显示输入的每条序列预测结果，包含最终的预测位置及准确率。
  * Fasta数据规范化处理：若Fasta序列中包含换行、空行，程序将自动处理成规范格式;
  * RNA格式检查：若用户输入的序列中包含“NA”字符，将会弹出警告提示；
  
* 结果以表格展示；
* 实验相关数据文件和模型文件说明参考：http://server.malab.cn/MMCOP/document.jsp

###3. 升级日志

* 2016-11-27 version0.1
  * 部署网站

