/**
 * 
 */

function getTurnNumber() 
{
	turnNumber = document.getElementById("turnNumber").innerHTML;
	url = "Reload?turnNumber=" + turnNumber;
	
	// Create the game page URL
	gamePage = "http://" + window.location.host + "/OthelloOnline/GamePage";
	
	
	var xhttp = new XMLHttpRequest();
	  xhttp.onreadystatechange = function() {
	    if (this.readyState == 4 && this.status == 200) {
    	
	    	if (this.responseText == "true")
	    	{
	    		window.location.href = gamePage;
	    	}
	    		
	    }
	  };
	  xhttp.open("GET", url, true);
	  xhttp.send();
}


setInterval(getTurnNumber, 1000);