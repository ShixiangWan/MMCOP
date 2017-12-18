<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE>
<html>
    <head>
        <base href="<%=basePath%>">
        <link rel="shortcut icon" href="icon.ico" type="image/x-icon" />
        <title>MMCOP</title>
	    <link href="css/bootstrap.min.css" rel="stylesheet">
        <script src="js/jquery-2.1.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <style type="text/css">
	        body {
	                padding-top: 100px;
	            }
        </style>
        <script type="text/javascript">
            function note() {
                $("#note").html("Please wait a moment. Submitting data ... ");
            }
        </script>
  </head>
  
  <body>
  		<!-- 导航栏 -->
    	<jsp:include page="header.jsp" />
    	
    	<div class="container">
    		<div class="row well">

                <h2>Data preprocessing</h2>
                <p>
                    The raw data are available here: The Cancer Genome Atlas (TCGA) pilot project (<a target="_blank" href="https://gdc-portal.nci.nih.gov/">https://gdc-portal.nci.nih.gov/</a>)
                    <br/>All the raw data were processed by three steps of preprocessing:
                    <ol>
                        <li>impute NA: impute.knn function;</li>
                        <li>Normalization: normalizeBetweenArrrays (method = “quantile”);</li>
                        <li>log2 transformation.</li>
                    </ol>
                All these three steps are embedded in limma (<a target="_blank" href="http://www.bioconductor.org/packages/release/bioc/html/limma.html">http://www.bioconductor.org/packages/release/bioc/html/limma.html</a>) Package in R.
                </p>

                <h2>1st Feature selection:</h2>
                <p>
                    The 1st feature selection is aimed to select miRNAs or CpGs which shows:
                    <ol>
                        <li>an abnormally expression/methylated value in a given normal tissue compared with other normal tissue types (one versus all, threshold: P-value ≤0.01);</li>
                        <li>the same value in a given cancer tissue when compared with the corresponding normal tissues (one versus all, threshold: P-value ≥ 0.5);</li>
                        <li>an abnormally expression value for the corresponding cancer type when compared with other tumor tissues (one versus all, threshold: P-value ≤0.01).</li>
                    </ol>
                </p>

                <h2>2nd Feature Selection:</h2>
                <p>
                    For DNAm profiles, 2nd feature selection was conducted by Maximum-Relevance-Maximum-Distance (MRMD: <a target="_blank" href="http://lab.malab.cn/soft/MRMD/index_en.html">http://lab.malab.cn/soft/MRMD/index_en.html</a>).
                    <br/>Another method of 2nd feature selection is Principal Component Analysis (PCA, embedded in the Dimensionality Reduction part of scikit-learn, <a target="_blank" href="http://scikit-learn.org/stable/index.html">http://scikit-learn.org/stable/index.html</a>), this method has not been put in the webserver.
                </p>

                <h2>Classifier Construction:</h2>
                <p>
                    All the selected biomarkers were followed by a random forest to construct the classifier, all individual models were available here:
                    <br/>For miRNA-based classifier: <u>miR-models</u> (<a href="download.do?type=miRNAmodel">download</a>)
                    <br/>For DNAm-based classifier: <u>DNAm-models</u> (<a href="download.do?type=DNAmodel">download</a>)
                    <br/>
                    <br/>The miRNAs and CpGs selected by the feature selection are available here: <u>Selected.CpGs.byMRMD.xlsx</u> (<a href="download.do?type=SelectedCpGs">download</a>); <u>Selected.miRNAs.xlsx</u> (<a href="download.do?type=SelectedmiRNAs">download</a>)
                    <br/>The annotation file for the CpGs is available here: <u>annotationCpGs_Infinium450K.txt</u> (<a href="download.do?type=annotationCpGs">download</a>)
                </p>


			</div>
            <!-- 底部栏 -->
            <hr style="width:100%;margin:3px;"/>
            <p style="text-align:center;" class="text-muted">Bioinformatics Laboratory - Tianjin University @ <a target="_blank" href="http://lab.malab.cn/~shixiang/">Shixiang Wan</a></p>
        </div>
  </body>
</html>
