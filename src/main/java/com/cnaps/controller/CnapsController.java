package com.cnaps.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cnaps.model.BulkValidation;
import com.cnaps.model.Payment;
import com.cnaps.model.SortPaymentProvider;
import com.cnaps.model.Type;
import com.cnaps.model.UserCredentials;
import com.cnaps.repository.CnapsRepository;
import com.cnaps.service.BulkServiceOTP;
import com.cnaps.service.CnapsServie;
import com.cnaps.service.KeyCloakService;
import com.cnaps.service.ServiceSortPaymentProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.SneakyThrows;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class CnapsController {
	@Autowired
	private CnapsServie cnapsService;
	
	@Autowired
	private CnapsRepository cnapsRepository;
	
	@Autowired
	private ServiceSortPaymentProvider serviceSortPaymentProvider;
	
	@Autowired
	KeyCloakService keyCloackService;

	 @Autowired
    private BulkServiceOTP bulkServiceOTP;
    @Autowired
    private final GoogleAuthenticator gAuth;

    public CnapsController(GoogleAuthenticator gAuth){
        this.gAuth = gAuth;
    }

	@GetMapping("/Historique")
	@CrossOrigin
	public List<Payment> lire() {
		return cnapsService.Lire();
	}
	
	@GetMapping("/Historique-list")
	@CrossOrigin
	  public ResponseEntity < ? > getStudents() {
	    Map < String, Object > respPay = new LinkedHashMap < String, Object > ();
	    List < Payment > listpayment = cnapsService.Lire();
	    if (!listpayment.isEmpty()) {
	      respPay.put("status", 1);
	      respPay.put("data", listpayment);
	      return new ResponseEntity < > (respPay, HttpStatus.OK);
	    } else {
	    	respPay.clear();
	    	respPay.put("status", 0);
	    	respPay.put("message", "Data is not found");
	      return new ResponseEntity < > (respPay, HttpStatus.NOT_FOUND);
	    }
	  }
	
	@PostMapping("/CSV-import")
	@CrossOrigin
	public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file) {
		try {
			List<Payment> paymentok = new ArrayList<>();
			BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line;
			boolean isFirstLine = true; // variable pour savoir si on est sur la première ligne
			while ((line = br.readLine()) != null) {
				if (isFirstLine) { // si on est sur la première ligne
					isFirstLine = false; // on passe la variable à false
					continue; // on passe à la ligne suivante
				}
				
				String[] fields = line.split(";");
				if (fields.length < 6) { // Vérifier si le tableau contient suffisamment d'éléments //mila misy contenue daholo raha eny apvoany no tsisy dia mbola mety
			        continue; // Si ce n'est pas le cas, passer à la ligne suivante
			    }
				BigInteger bigIntNumber = new BigInteger(fields[3]);
				Payment data = new Payment();
				data.setReference(fields[0]);
				data.setNomBeneficiaire(fields[1]);
				data.setNumeroCompteBeneficiaire(fields[2]);
				data.setMontant(bigIntNumber);
				data.setDevise(fields[4]);
				data.setDateExecution(fields[5]);
				data.setType(fields[6]);
				data.setStatut(fields[7]);
				paymentok.add(data);
			}
			
			for (Payment Payment : paymentok) {
				cnapsService.Creer(Payment);
			}
			// Enregistrer les transactions dans la base de données ou effectuer des
			// traitements supplémentaires ici
			return ResponseEntity.ok("Le fichier CSV a été importé avec succès.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Une erreur s'est produite lors de l'importation du fichier CSV : " + e.getMessage());
		}
	}
	
	@PostMapping("/CSV-import-date")
	@CrossOrigin
	public ResponseEntity<?> uploadCSVFileDate(@RequestParam("file") MultipartFile file, @RequestParam("dateExecution") String dateExecution, @RequestParam("Type") Type type) {
		try {
			List<Payment> paymentok = new ArrayList<>();
			BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String line;
			boolean isFirstLine = true; // variable pour savoir si on est sur la première ligne
			while ((line = br.readLine()) != null) {
				if (isFirstLine) { // si on est sur la première ligne
					isFirstLine = false; // on passe la variable à false
					continue; // on passe à la ligne suivante
				}
				
				String[] fields = line.split(";");
				if (fields.length < 9) { // Vérifier si le tableau contient suffisamment d'éléments //mila misy contenue daholo raha eny apvoany no tsisy dia mbola mety
			        continue; // Si ce n'est pas le cas, passer à la ligne suivante
			    }
				BigInteger bigIntNumber = new BigInteger(fields[5]);
				Payment data = new Payment();
				data.setReference(fields[0]);
				data.setidClient(fields[1]);
				data.setNomBeneficiaire(fields[2]);
				data.setNumeroCompteBeneficiaire(fields[3]);
				data.setNumeroCompteAdebiter(fields[4]);
				data.setMontant(bigIntNumber);
				data.setDevise(fields[6]);
				data.setDateExecution(dateExecution);
				data.setType(type.name().toString());
				data.setStatut(fields[9]);
				paymentok.add(data);
			}
			
			for (Payment Payment : paymentok) {
				cnapsService.Creer(Payment);
			}
			// Enregistrer les transactions dans la base de données ou effectuer des
			// traitements supplémentaires ici
			return ResponseEntity.ok(paymentok);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Une erreur s'est produite lors de l'importation du fichier CSV : " + e.getMessage());
		}
	}
	// Ajout QR code avant upload
	@PostMapping("/validate")
    public ResponseEntity<String> validateCode(@RequestBody BulkValidation bulkValidation) {
        boolean isValid = bulkServiceOTP.isCodeValid(bulkValidation);
        if (isValid) {
            return ResponseEntity.ok("Code validé avec succès.");
        } else {
            return ResponseEntity.status(401).body("Code invalide.");
        }
    }
       @SneakyThrows
       @GetMapping("/generate")
       public void generate(HttpServletResponse response) throws WriterException, IOException {
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        String cle = key.getKey();
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String otpAuthURL = GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("clé",cle , key);
        BitMatrix bitMatrix = qrCodeWriter.encode(otpAuthURL, BarcodeFormat.QR_CODE, 200, 200);
		response.setContentType("image/png");
        ServletOutputStream outputStream = response.getOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        outputStream.close();
    }

	// fin QRcode

	
	@PutMapping("/process-payment-cnaps")
	@CrossOrigin
	public ResponseEntity<Payment> updatePaymentStatus(@RequestParam String reference, @RequestParam String statut) {
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		// Ajoutez 3 heures
		LocalDateTime newDateTime = currentDateTime.plusHours(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDateTime = newDateTime.format(formatter);
		
		cnapsRepository.updatePaymentStatus(statut, formattedDateTime, reference);
		Payment updatedPayment = cnapsRepository.findByReference(reference);

        if (updatedPayment != null) {
            // Si la ligne a été mise à jour avec succès, retournez-la avec le code de statut HTTP 200 OK
            return ResponseEntity.ok(updatedPayment);
        } else {
            // Si la mise à jour a échoué, retournez une réponse avec un code de statut HTTP 404 Not Found
            return ResponseEntity.notFound().build();
        }
	}
	
	@PutMapping("/sort-payment-cnaps")
	@CrossOrigin
	public ResponseEntity<SortPaymentProvider> updatePaymentStatusFinal(@RequestParam String referenceProvider,@RequestParam String reference,
			@RequestParam String referenceVirement,@RequestParam String statut, @RequestParam String solde, @RequestParam String libelle) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		// Ajoutez 3 heures
		LocalDateTime newDateTime = currentDateTime.plusHours(3);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //HH:mm:ss
		String formattedDateTime = newDateTime.format(formatter);
		
		cnapsRepository.updatePaymentStatus(statut, formattedDateTime, reference);
		Payment updatedPayment = cnapsRepository.findByReference(reference);
		
		SortPaymentProvider payment = new SortPaymentProvider();
		payment.setReferenceVirement(referenceVirement);
		payment.setReferenceProvider(referenceProvider);
		payment.setStatut(statut);
		payment.setMontant(updatedPayment.getMontant());
		payment.setDate();
		payment.setLibelle(libelle);
		payment.setReferenceInterne(reference);
		payment.setSolde(solde);
		
		serviceSortPaymentProvider.nouvellevirement(payment);
		
        return ResponseEntity.ok(payment);
	}
	
	@PostMapping("/OAuth-cnaps")
	@CrossOrigin
	public ResponseEntity<?> getTokenUsingCredentials(@RequestBody UserCredentials userCredentials) {

		String responseToken = null;
		try {

			responseToken = keyCloackService.getToken(userCredentials);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(responseToken);

			// Récupérez la valeur du champ "access_token"
			String accessToken = jsonNode.get("access_token").asText();

			// Créez un objet JSON avec le champ "access_token"
			ObjectMapper responseMapper = new ObjectMapper();
			ObjectNode jsonResponse = responseMapper.createObjectNode();
			jsonResponse.put("access_token", accessToken);
			return ResponseEntity.ok().body(jsonResponse);

		} catch (Exception e) {

			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// return new ResponseEntity<>(responseToken, HttpStatus.OK);

	}
	
	@GetMapping("/Historique-typedate")
	@CrossOrigin
	public List<Payment> HistoriqueTypeDate(@RequestParam("type") String type,
			@RequestParam("date") Date date) {
		return cnapsRepository.findByType(type, date.toString());
	}
	
	@GetMapping("/Historique-date")
	@CrossOrigin
	public List<Payment> HistoriqueDate(@RequestParam("date") Date date) {
		return cnapsRepository.findByDateExecution(date.toString());
	}
	
	@GetMapping("/Historique-day-success")
	@CrossOrigin
	public Long HistoriqueDaySuccess(@RequestParam("date") Date date) {
		return cnapsRepository.countByStatut(date.toString());
	}
	
	@GetMapping("/Historique-day-initiated")
	@CrossOrigin
	public Long HistoriqueDayInitiated(@RequestParam("date") Date date) {
		return cnapsRepository.countByStatutInitated(date.toString());
	}
}
