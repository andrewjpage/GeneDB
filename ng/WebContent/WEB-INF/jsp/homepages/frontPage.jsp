<%@ include file="/WEB-INF/jsp/topinclude.jspf" %>
<%@ taglib prefix="db" uri="db" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="misc" uri="misc" %>
<format:header title="Homepage" />
<format:page>
<br>

<misc:url value="/Homepage/" var="base2" />
<div id="col-1-1">
<h1>Datasets</h1>
<div class="main-light-grey-top"></div>
<div class="main-light-grey">
<table cellpadding="0" cellspacing="0" width="100%" class="dataset-table">
<tr>
<td align="center" valign="top" width="50%">
<h3>Apicomplexan Protozoa</h3>
<img src="<misc:url value="/includes/image/dataset-apicomplexan-protozoa.jpg"/>" height="163" width="136" alt="Apicomplexan Protozoa" />
<db:homepageselect top="Apicomplexa" baseUrl="${base2}" />
</td>
<td align="center" valign="top" width="50%">

<h3>Kinetoplastid Protozoa</h3>
<img src="<misc:url value="/"/>includes/image/dataset-kinetoplastid-protozoa.jpg" height="163" width="136" alt="Kinetoplastid Protozoa" />
<db:homepageselect top="Kinetoplastida" baseUrl="${base2}" leafOnly="true" />
</td>
</tr>

<tr>
<td align="center" valign="top" width="50%">
<h3>Parasitic Helminths</h3>
<img src="<misc:url value="/"/>includes/image/dataset-parasitic-helminths.jpg" height="163" width="136" alt="Parasitic Helminths" />
<db:homepageselect top="Helminths" baseUrl="${base2}" leafOnly="true" />
</td>
<td align="center" valign="top" width="50%">
<h3>Parasite Vectors</h3>
<img src="<misc:url value="/includes/image/dataset-parasite-vectors.jpg"/>" height="163" width="136" alt="Parasite Vectors" />
<select name="organism" onChange="if (this.selectedIndex != 0) {document.location.href=this.value }"><option value="none">Select an organism</option><option value="${baseUrl}Page/parasiteVectors">Parasite Vectors</option></select>
</td>

</tr>
</table>
</div>
<div class="main-light-grey-bot"></div>

<div id="col-3-1">
<h1>Sequence searches</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
<p class="block-para">Blast <br />

&raquo; Single organism <span class="dataset-table"><db:homepageselect title="Select an organism" top="Root" baseUrl="/blast/submitblast/GeneDB_" leafOnly="true" alwaysLink="true"/></span> <br />

&raquo; <a href="<misc:url value="/blast/submitblast/GeneDB_proteins/omni" />">Multi-organism (proteins)</a><br />
&raquo; <a href="<misc:url value="/blast/submitblast/GeneDB_transcripts/omni" />">Multi-organism (transcripts and contigs/chromosomes)</a><br />

 </p>


</div>
<div class="light-grey-bot"></div>
</div>

<div id="col-3-2">
<h1>Go to our&hellip;</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
&raquo; <a href="<misc:url value="/Query" />">Query page</a><br />
&raquo; <a href="<misc:url value="/web-artemis/"/>">Web Artemis</a><br />
&raquo; <a href="<misc:url value="/cgi-bin/amigo/go.cgi"/>">AmiGO</a><br />
&raquo; <a href="<misc:url value="/Page/jbrowse"/>">JBrowse</a><br />
&raquo; <a href="http://lists.sanger.ac.uk/mailman/listinfo/genedb-info">Mailing list</a>
</div>
<div class="light-grey-bot"></div>
</div>

</div><!-- end main content column -left -->

<div id="col-1-2">


<h1>Events</h1>
<div class="baby-blue-top"></div>
<div class="baby-blue">
<p class="block-para">
There are no events planned at this time.
</p>
</div>
<div class="baby-blue-bot"></div>


<h1>News</h1>
<div class="light-grey-top"></div>
<div class="light-grey">
<p class="block-para">
There are no current news at the moment.
</p>
</div>
<div class="light-grey-bot"></div> 

<h1>Web services</h1>
<div class="baby-blue-top"></div>
<div class="baby-blue">
<p>
are now available for GeneDB: <br />
&raquo; <a href="http://www.genedb.org/services">Web services</a>
</p>
</div>
<div class="baby-blue-bot"></div>

<h1>Information</h1>
<div class="baby-blue-top"></div>
<div class="baby-blue">

<p>Data <br />
&raquo; <a href="<misc:url value="/Page/releases"/>">Data Release Policy</a><br />
</p>
<br />
<p>Sequencing Projects <br />
&raquo; <a href="http://www.sanger.ac.uk/resources/downloads/protozoa/">Eukaryotes (Protozoa)</a><br />
</p>
<br />
<p>
Software<br />
&raquo; <a href="http://www.sanger.ac.uk/science/tools/artemis-comparison-tool-act">ACT</a><br />
&raquo; <a href="http://www.sanger.ac.uk/science/tools/artemis">Artemis</a><br />
</p>
<br />
<p>
Contributors<br />
&raquo; <a href="<misc:url value="/Page/acknowledgements" />">Acknowledgements</a><br />
</p>

</div>
<div class="baby-blue-bot"></div>
</div><!-- end sidebar content columb -right -->

</format:page>
