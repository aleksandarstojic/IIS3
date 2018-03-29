package rva.ctrls;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import rva.jpa.Porudzbina;
import rva.reps.PorudzbinaRepository;

@RestController
@Api(tags = {"Porudžbina CRUD operacije"})
public class PorudzbinaRestController {
	
	@Autowired
	private PorudzbinaRepository porudzbinaRepository; 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("porudzbina")
	@ApiOperation(value = "Vraća kolekciju svih porudžbina iz baze podataka")
	public Collection<Porudzbina> getPorudzbine(){
		return porudzbinaRepository.findAll(); 
	}
	
	@GetMapping("porudzbinaId/{id}")
	@ApiOperation(value = "Vraća porudžbinu iz baze podataka čiji je id vrednost prosleđena kao path varijabla")
	public Porudzbina getPorudzbina(@PathVariable("id") Integer id) {
		return porudzbinaRepository.getOne(id);
	}
	
	@GetMapping("porudzbinePlacene")
	@ApiOperation(value = "Vraća kolekciju porudžbina iz baze podataka koje u nazivu sadrže string prosleđen kao path varijabla")
	public Collection<Porudzbina> getPorudzbineByPlaceno(){
		return porudzbinaRepository.findByPlacenoTrue();
	}
	
	@Transactional
	@DeleteMapping("porudzbinaId/{id}")
	@ApiOperation(value = "Briše porudžbinu iz baze podataka čiji je id vrednost prosleđena kao path varijabla")
	public ResponseEntity<Porudzbina> deletePorudzbina(@PathVariable ("id") Integer id){
		if(!porudzbinaRepository.existsById(id))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		jdbcTemplate.execute("delete from stavka_porudzbine where porudzbina = "+id);
		porudzbinaRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//insert - Ovde koristimo POST metodu
	@PostMapping("porudzbina")
	@ApiOperation(value = "Upisuje porudžbinu u bazu podataka")
	public ResponseEntity<Porudzbina> insertPorudzbina(@RequestBody Porudzbina porudzbina){
		if(porudzbinaRepository.existsById(porudzbina.getId())) {
			return new ResponseEntity<> (HttpStatus.CONFLICT);
		}
		porudzbinaRepository.save(porudzbina);
		return new ResponseEntity<>(HttpStatus.OK);
	}
		
		// update - Ovde koristimo PUT metodu
	@PutMapping("porudzbina")
	@ApiOperation(value = "Modifikuje postojeću porudžbinu u bazi podataka")
	public ResponseEntity<Porudzbina> updatePorudzbina(@RequestBody Porudzbina porudzbina){
		if(porudzbinaRepository.existsById(porudzbina.getId())) {
			porudzbinaRepository.save(porudzbina);
			return new ResponseEntity<> (HttpStatus.OK);
		}
			return new ResponseEntity<> (HttpStatus.NO_CONTENT);
		}
}
