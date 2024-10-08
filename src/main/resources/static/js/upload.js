let table;
let demandeSuccess = document.getElementById("demandeSuccess");
let demandeInitiated = document.getElementById("demandeInitiated");
let titreViremment = document.getElementById("titreViremment");

ViremmentDaySuccess();
setInterval(ViremmentDaySuccess, 1000);
ViremmentDayInitiated();
setInterval(ViremmentDayInitiated, 1000);

function uploadFileWithTodayDate() {
	const today = new Date().toISOString().split('T')[0];
	const type = "IBULK";
	uploadFile(today,type);
}

//QRcode js

//fin QRcode

function valider(){
    window.location.href="QRcode.html"
}

function uploadFileWithTomorrowDate() {
	const tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	const dateTomorrow = tomorrow.toISOString().split('T')[0];
	const type = "NBULK";
	uploadFile(dateTomorrow,type);
}

function uploadFileWithUserDate() {
	const userDate = document.getElementById('date-execution').value;
	const type = "FBULK";
	if (!userDate) {
		alert("Veuillez entrer une date.");
		return;
	}
	uploadFile(userDate, type);
}

async function uploadFile(dateExecution, type) {
    const fileInput = document.getElementById('csv-file');
    const file = fileInput.files[0];
    Effacer(); // Clear existing table

    if (!file) {
        alert('Veuillez sélectionner un fichier CSV à importer.');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    

    // Construct API URL with parameters
    const url = new URL('/api/CSV-import-date', window.location.origin);
    
    url.searchParams.append('dateExecution', dateExecution);
    url.searchParams.append('Type', type);

    try {
        const response = await fetch(url, {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json(); // Parse JSON response
        document.getElementById('response-message').innerText = 'Erreur lors de l\'importation du fichier: ' + error.message;

        console.log(data); // Log response data

        // Display response data in the table
        Affichage(data);
        //document.getElementById('response-message').innerText = 'Importation réussie!';
    } catch (error) {
        console.error('Erreur lors de l\'importation du fichier:', error);
    }
}

function ListeBulk() {
  
    Effacer();
    const apiUrl = '/api/Historique';
    $.ajax({
		url: `${apiUrl}`, 
        method: 'GET',
        dataType: 'json',
        success: function (data) {
			console.log(data);
            Affichage(data);
            
            // Réactivez la fonction une fois qu'elle a terminé son travail
            enCoursExecution = false;
        },
        error: function (error) {
            console.error(error);
            // Réactivez la fonction même en cas d'erreur pour permettre les futurs appels
            enCoursExecution = false;
        }
    });
}

function ListeIbulk() {
    Effacer();
    const apiUrl = '/api/Historique-date';
    
    // Récupérer la date d'aujourd'hui au format ISO (YYYY-MM-DD)
    const today = new Date().toISOString().split('T')[0];
    
    // Définir le type
    const type = 'IBULK';
    
    // Construire l'URL avec les paramètres de requête
    //const urlWithParams = `${apiUrl}?date=${today}&type=${type}`;
    const urlWithParams = `${apiUrl}?date=${today}`;
    
    $.ajax({
        url: urlWithParams,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            Affichage(data);
            
            // Réactivez la fonction une fois qu'elle a terminé son travail
            enCoursExecution = false;
        },
        error: function (error) {
            console.error(error);
            // Réactivez la fonction même en cas d'erreur pour permettre les futurs appels
            enCoursExecution = false;
        }
    });
}


function ListeFbulk() {
    Effacer();
    const apiUrl = '/api/Historique-date';
    
    // Récupérer la date d'aujourd'hui
    let today = new Date();
    
    // Ajouter un jour
    today.setDate(today.getDate() + 1);
    
    // Convertir la nouvelle date en format ISO (YYYY-MM-DD)
    const tomorrow = today.toISOString().split('T')[0];
    
    // Définir le type
    const type = 'FBULK';
    
    // Construire l'URL avec les paramètres de requête
    //const urlWithParams = `${apiUrl}?date=${tomorrow}&type=${type}`;
    const urlWithParams = `${apiUrl}?date=${tomorrow}`;
    
    $.ajax({
        url: urlWithParams,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            Affichage(data);
            
            // Réactivez la fonction une fois qu'elle a terminé son travail
            enCoursExecution = false;
        },
        error: function (error) {
            console.error(error);
            // Réactivez la fonction même en cas d'erreur pour permettre les futurs appels
            enCoursExecution = false;
        }
    });
}

function ListeNbulk() {
    Effacer();
    const apiUrl = '/api/Historique-date';
    
    // Récupérer la date d'aujourd'hui
   const date = document.getElementById('date-nbulk').value;
    
    // Définir le type
    const type = 'NBULK';
    
    // Construire l'URL avec les paramètres de requête
    //const urlWithParams = `${apiUrl}?date=${date}&type=${type}`;
    const urlWithParams = `${apiUrl}?date=${date}`;
    
    $.ajax({
        url: urlWithParams,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            console.log(data);
            Affichage(data);
            
            // Réactivez la fonction une fois qu'elle a terminé son travail
            enCoursExecution = false;
        },
        error: function (error) {
            console.error(error);
            // Réactivez la fonction même en cas d'erreur pour permettre les futurs appels
            enCoursExecution = false;
        }
    });
}

async function Effacer() {
    table = $('#basic-datatables').DataTable();
    table.clear().destroy();
}

//Afichage
async function Affichage(data) {
    table = $('#basic-datatables').DataTable({
        ordering: false,
        data: data,
        columns: [
            {data: 'reference', title: 'Reference'},
            {data: 'idClient', title: 'Id Client'},
            {data: 'nomBeneficiaire', title: 'Nom Beneficiaire'},
            {data: 'numeroCompteBeneficiaire', title: 'N° Compte Beneficiaire'},
            {data: 'numeroCompteAdebiter', title: 'N° Compte à debiter'},
            {data: 'montant', title: 'Montant'},
            {data: 'devise', title: 'Devise'},
            {data: 'dateExecution', title: 'Date Execution'},
            {data: 'type', title: 'Type'},
            {data: 'statut', title: 'Statut'}
        ],
        order: [
            [0, 'desc']
        ]
    });
}


function logout() {
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // Assurez-vous que le serveur peut accepter JSON si nécessaire
        },
        body: JSON.stringify({}) // Envoyez éventuellement des données JSON supplémentaires si nécessaire
    })
    .then(response => {
    
            // La déconnexion a réussi, redirigez l'utilisateur vers la page d'accueil ou une autre page
            window.location.href = 'accueil.html';
    })
    .catch(error => {
        console.error('Erreur lors de la déconnexion:', error);
    });
}


function ViremmentDaySuccess() {
    const currentTimestamp = Date.now();
    const formattedDate = formatDateFromTimestamp(currentTimestamp);
    const urlWithParams = `/api/Historique-day-success?date=${formattedDate}`;

    fetch(urlWithParams, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erreur réseau');
        }
        return response.json(); // Transforme la réponse en JSON
    })
    .then(data => {
        totalSuccess(data);
    })
    .catch(error => {
        console.error('Erreur:', error);
    });
}

function ViremmentDayInitiated() {
    const currentTimestamp = Date.now();
    const formattedDate = formatDateFromTimestamp(currentTimestamp);
    const urlWithParams = `/api/Historique-day-initiated?date=${formattedDate}`;

    fetch(urlWithParams, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erreur réseau');
        }
        return response.json(); // Transforme la réponse en JSON
    })
    .then(data => {
        totalInitiated(data);
    })
    .catch(error => {
        console.error('Erreur:', error);
    });
}

function formatDateFromTimestamp(timestamp) {
	const date = new Date(timestamp);
	const year = date.getFullYear();
	const month = (date.getMonth() + 1).toString().padStart(2, '0');
	const day = date.getDate().toString().padStart(2, '0');
	return `${year}-${month}-${day}`;
}

function totalSuccess(ttl) {
	demandeSuccess.innerHTML = "";
	titreViremment.innerHTML = "";
	const r2 = document.createElement('p');
	r2.innerHTML = "Viremment";
	r2.style.fontWeight = "bold";
	r2.style.fontSize = "20px";
	r2.style.textAlign = "center";
	titreViremment.appendChild(r2);
	const r1 = document.createElement('p');
	r1.innerText = "SUCCESS : "+ttl;
	r1.style.fontWeight = "bold";
	r1.style.fontSize = "20px";
	r1.style.textAlign = "center";
	r1.style.color = "#00ff00";
	demandeSuccess.appendChild(r1);
}

function totalInitiated(ttl) {
	demandeInitiated.innerHTML = "";
	const r1 = document.createElement('p');
	r1.innerText = "INITIATED : "+ttl;
	r1.style.fontWeight = "bold";
	r1.style.fontSize = "20px";
	r1.style.textAlign = "center";
	r1.style.color = "#FF0000";
	demandeInitiated.appendChild(r1);
}

/*document.getElementById('upload-form').addEventListener('submit', function(event) {
	event.preventDefault();
	const fileInput = document.getElementById('csv-file');
	const dateInput = document.getElementById('date-execution'); // Accéder au champ de date
	const file = fileInput.files[0];
	const dateExecution = dateInput.value; // Récupérer la valeur de la date

	if (!file) {
		alert('Veuillez sélectionner un fichier CSV à importer.');
		return;
	}

	const formData = new FormData();
	formData.append('file', file);

	// Construction de l'URL avec le paramètre DateExecution
	const url = new URL('/api/CSV-import-date', window.location.origin);
	url.searchParams.append('dateExecution', dateExecution);

	fetch(url, {
		method: 'POST',
		body: formData,
	})
	.then(response => response.text())
	.then(data => {
		document.getElementById('response-message').innerText = data;
	})
	.catch(error => {
		console.error('Erreur lors de l\'importation du fichier:', error);
		document.getElementById('response-message').innerText = 'Erreur lors de l\'importation du fichier: ' + error.message;
	});
});

document.getElementById('upload-form').addEventListener('submit', function(event) {
	event.preventDefault();
	const fileInput = document.getElementById('csv-file');
	const file = fileInput.files[0];

	if (!file) {
		alert('Veuillez sélectionner un fichier CSV à importer.');
		return;
	}

	const formData = new FormData();
	formData.append('file', file);

	fetch('/api/CSV-import', {
		method: 'POST',
		body: formData,
	})
	.then(response => response.text())
	.then(data => {
		document.getElementById('response-message').innerText = data;
	})
	.catch(error => {
		console.error('Erreur lors de l\'importation du fichier:', error);
		document.getElementById('response-message').innerText = 'Erreur lors de l\'importation du fichier: ' + error.message;
	});
});
*/