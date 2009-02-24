<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<format:headerRound title="Gene Type Search">
	<st:init />
	<link rel="stylesheet" type="text/css" href="<c:url value="/includes/style/genedb/genePage.css"/>" />
</format:headerRound>
<br>
<div id="geneDetails">
	<format:genePageSection id="nameSearch" className="whiteBox">
		<form:form commandName="query" action="Query" method="post">
        <input type="hidden" name="q" value="geneType" />
            <table>
                <tr>
                    <td colspan="4">
                        <font color="red"><form:errors path="*" /></font>
                    </td>
                </tr>
                <tr>
                <td><br><big><b>Gene Type Search Search:&nbsp;</b></big></td>
                <td>
                     <b>Organism:</b>
            	     <br><db:simpleselect />
            	     <br><font color="red"><form:errors path="taxons" /></font>
                  <td>
                  <td>
                  	<b>Gene Type:</b>
                  <br>
                    <form:select path="type">
                        <c:forEach items="${typeMap}" var="mapEntry">
                        <form:option value="${mapEntry.key}">${mapEntry.value}</form:option>
                        </c:forEach>
                    </form:select>
                  </td>
                  <td >
                    <br><input type="submit" value="Submit" />
                  </td>
                </tr>
            </table>

		</form:form>
	</format:genePageSection>
</div>


<br><query:results />
<format:footer />