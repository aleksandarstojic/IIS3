package rva.ctrls;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rva.jpa.Artikl;
import rva.reps.ArtiklRepository;

@RestController
@Api(tags = {"Artikl CRUD operacije"}) 
public class ArtiklRestController {
	@Autowired
	private ArtiklRepository artiklRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("artikl")
	@ApiOperation(value = "Vrаća kolekciju svih artikala iz baze podataka")
	public Collection<Artikl> getArtikli(){
		return artiklRepository.findAll(); 
	}
	
	@GetMapping("artiklId/{id}")
	@ApiOperation(value = "Vrаća artikl iz baze podataka ciji je ID vrednost prosleđena kao path varijabla")
	public Artikl getArtikl(@PathVariable("id") Integer id) {
		return artiklRepository.getOne(id);
	}
	
	@GetMapping("artiklNaziv/{naziv}")
	@ApiOperation(value = "Vrаća artikl iz baze podataka koji u naziv sadrzi string prosleđen kao path varijabla")
	public Collection<Artikl> getArtiklByNaziv(@PathVariable("naziv") String naziv){
		return artiklRepository.findByNazivContainingIgnoreCase(naziv);
	}
	
	@DeleteMapping("artiklId/{id}")
	@ApiOperation(value = "Briše artikl iz baze podataka ciji je ID vrednost prosleđena kao path varijabla")
	public ResponseEntity<Artikl> deleteArtikl(@PathVariable("id") Integer id){
		if(artiklRepository.existsById(id)) {
			artiklRepository.deleteById(id);
			if(id == -100)
				jdbcTemplate.execute("INSERT INTO \"artikl\"(\"id\", \"naziv\", \"proizvodjac\")\r\n" + 
									 "VALUES(-100, 'Test SoapUI artikl', 'Test proizvodjac')");
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	//insert - Ovde koristimo POST metodu
	@PostMapping("artikl")
	@ApiOperation(value = "Insertuje artikl u bazu podataka")
	public ResponseEntity<Artikl> insertArtikl(@RequestBody Artikl artikl){
		if(artiklRepository.existsById(artikl.getId())) {
			return new ResponseEntity<> (HttpStatus.CONFLICT);
		}
		artiklRepository.save(artikl);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//update - Ovde koristimo PUT metodu
	@PutMapping("artikl")
	@ApiOperation(value = "Modifikuje artikl iz baze podataka")
	public ResponseEntity<Artikl> updateArtikl(@RequestBody Artikl artikl){
		if(artiklRepository.existsById(artikl.getId())) {
			artiklRepository.save(artikl);
			return new ResponseEntity<> (HttpStatus.OK);
		}
		return new ResponseEntity<> (HttpStatus.NO_CONTENT);
	}
}
