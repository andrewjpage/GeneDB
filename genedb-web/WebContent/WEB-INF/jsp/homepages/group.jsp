<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<format:header title="Homepage" />
<format:page>
<br>

<style>
.readableText
{
    font-size: 1.2em;
    line-height:1.5em;
    text-align:justify;
    margin-left:1em;
}
.readableText a
{
    color:#4381a7;
}
ul {
    padding-left:1em;
}
</style>


  
<div id="col-1-1"> 
    <div class="readableText">
        <h1>${label} genome projects</h1>
        <c:catch var="e">
            <jsp:include page="organisms/${label}.jsp" />
        </c:catch>
        <c:if test="${e!=null}">
            Here is a list of ${label} genomes maintained in the database.
        </c:if>
    </div>
</div>

<div id="col-1-2"> 
    <h2>${label} in GeneDB</h2>
    <div class="baby-blue-top"></div>
        <div class="baby-blue">
            <db:phylonodehomepagelisttag baseUrl="${base2}" top="${label}" />
        </div>
    <div class="baby-blue-bot"></div>

    <h2>About GeneDB</h2>
    <div class="baby-blue-top"></div>
        <div class="baby-blue">
            <P><I>GENEDB is a database that provides a window into ongoing annotation and curation at the Sanger Institute.</I></P>
        </div>
    <div class="baby-blue-bot"></div>
    <br><br><br>
</div>


</format:page>
