<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Upload Result</title>

</head>
<body>
<h3>Uploaded Files:</h3>
Description: <span>${description}</span>
<br/>
<c:forEach var="file" items="${uploadedFiles}">
    <span>${file}</span>
</c:forEach>
</body>
</html>
