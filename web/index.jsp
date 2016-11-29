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
    		<div class="row">
				<div class="col-md-4">
					<img src="img/flowchart.jpg" style="width:360px;"/>
				</div>
				<div class="col-md-8">
					<div style="font-size:36px; padding: 0;">miRNA and Methylation CUP Origin Predictor</div>
					<h4 class="text-muted"></h4>
					<hr style="width:100%;margin:3px;"/>
					<form class="form-horizontal" action="predict.do" method="post" enctype="multipart/form-data">
						<div class="form-group">
                            <%--粘贴和上传组件--%>
                            <textarea name="paste" class="form-control" rows="3" id="textArea" style="height:115px;"></textarea>
                            <span class="help-block">Paste your miRNA expression or DNAm profiles, <strong>OR UPLOAD YOUR FILE BELOW (maximum 100MB)</strong>
                            <input name="file" type="file" id="predictInputFile">
                            </span>
                            Example Download:
                            <a href="download.do?type=miRNA"><strong>miRNA;</strong></a>
                            <a href="download.do?type=DNAm"><strong>DNA methylation</strong></a>
                            <br/>
                            <%--预测选项--%>
                            Predict Option:
                            <label class="radio-inline" style="padding-top: 0">
                                <input name="type" type="radio" id="inlineRadio1" value="miRNA" checked> miRNA
                            </label>
                            <label class="radio-inline" style="padding-top: 0">
                                <input name="type" type="radio" id="inlineRadio2" value="DNAm"> DNA methylation
                            </label>
                            <p style="margin: 5px 0 0 0;"></p>
                            <button type="submit" onclick="note()" class="btn btn-primary" style="width: 300px;">Predict</button>
                            <strong><span id="note" style="color: darkmagenta;">${requestScope.error}</span></strong>
                        </div>
                    </form>
				</div>

                <h1 style="height: 325px;"></h1>
                <c:if test="${empty requestScope.map}">
                    <!-- 简介模块 -->
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <div class="col-md-4">
                                <h4>Note:</h4>
                                <p>
                                    (1) This webserver supports 14 tissues for miRNA expression profiles: Bladder, Breast, Bile Duct, Colorectal, Esophagus, Head and Neck, Kidney Chromophobe, Kidney Renal Clear Cell, Liver, Lung, Prostate, Stomach, Thyroid, Uterus; and 14 tissues for DNA methylation profiles: Bladder, Breast, Colorectal, Esophagus, Head and Neck, Kidney Renal Clear Cell, Kidney Renal Papillary Cell, Liver, Lingual squamous cell, Lung, Pancreas, Prostate, Thyroid, Uterus;
                                    <br/>(2) Be sure your uploaded data have been processed by imputing NA, Normalization and log2 transformation.
                                </p>
                            </div>
                            <div class="col-md-4">
                                <h4>Feature Selection and Random forest:</h4>
                                <p>
                                    miRNA-based Classifier: 1-level feature selection (differential miRNAs expressed analysis) + random forest;
                                    <br/>DNAm-based Classifier: 2-level feature selection (differential CpGs methylated analysis+ Maximum-Relevance-Maximum-Distance (MRMD)) + random forest.
                                </p>
                            </div>
                            <div class="col-md-4">
                                <h4>Contact information:</h4>
                                <p>
                                    For the related research, please refer to the following manuscript:
                                    <br/><em>Wei Tang, Shixiang Wan, Quan Zou. Efficient Prediction of Tumor Origin through Identification of Tissue-Specific biomarkers based on miRNA expression and DNA methylation. [manuscript]</em>
                                    <br/>For the research issue, please contact: Wei Tang (wtang@tju.edu.cn)
                                    <br/>For the webserver issue, please contact: Shixiang Wan (shixiangwan@gmail.com )
                                </p>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty requestScope.map}">
                    <%--表格结果展示--%>
                    <h3>Predicted Result<small> 1 means this instance is classified in this tissue, while 0 means not.</small></h3>
                    <table class="table table-bordered table-hover table-condensed well">
                        <thead>
                        <tr>
                            <th>Your Instances Order</th>
                            <c:forEach items="${head}" var="v">
                                <td>${v}</td>
                            </c:forEach>
                        </tr>
                        </thead>
                        <tbody>
                        <c:set var="index" value="0" />
                        <c:forEach items="${requestScope.map}" var="m">
                            <c:set var="index" value="${index+1}" />
                            <tr>
                                <td>${m.key}</td>
                                <c:forEach items="${m.value}" var="v">
                                    <td style="text-align: center;">${v}</td>
                                </c:forEach>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>

				<!-- 引用模块 -->
				<%--<div class="col-md-12 alert alert-dismissible alert-info">
	                 <p><strong>Cite Halign Server in a publication:</strong><br>
	                     [1]. Quan Zou, Qinghua Hu, Maozu Guo, Guohua Wang. HAlign: Fast Multiple Similar DNA/RNA Sequence Alignment Based on the Centre Star Strategy. Bioinformatics. 2015,31(15): 2475-2481. (<a target="_bank" href="http://bioinformatics.oxfordjournals.org/cgi/reprint/btv177?ijkey=CbHd7aTXctZ4Ofv&keytype=ref">link</a>)
	                     <br>
	                     [2]. Quan Zou, Xubin Li, Wenrui Jiang, Ziyu Lin, Guilin Li, Ke Chen. Survey of MapReduce Frame Operation in Bioinformatics. Briefings in Bioinformatics. 2014,15(4): 637-647
	                 </p>
				</div>--%>

				<!-- 底部栏 -->
				<hr style="width:100%;margin:3px;"/>
				<p style="text-align:center;" class="text-muted">Bioinformatics Laboratory - Tianjin University @ <a target="_blank" href="http://lab.malab.cn/~shixiang/">Shixiang Wan</a></p>
			</div>
    	</div>
  </body>
</html>
