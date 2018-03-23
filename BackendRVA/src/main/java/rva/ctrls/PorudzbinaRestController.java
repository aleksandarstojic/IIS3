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

import rva.jpa.Dobavljac;
import rva.jpa.Porudzbina;
import rva.reps.PorudzbinaRepository;

@RestController
public class PorudzbinaRestController {
	
	@Autowired
	private PorudzbinaRepository porudzbinaRepository; 
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("porudzbina")
	public Collection<Porudzbina> getPorudzbine(){
		return porudzbinaRepository.findAll(); 
	}
	
	@GetMapping("porudzbinaId/{id}")
	public Porudzbina getPorudzbina(@PathVariable("id") Integer id) {
		return porudzbinaRepository.getOne(id);
	}
	
	@GetMapping("porudzbinePlacene")
	public Collection<Porudzbina> getPorudzbina(@PathVariable("naziv") String naziv){
		return porudzbinaRepository.findByPlacenoTrue();
	}
	
	@Transactional
	@DeleteMapping("porudzbinaId/{id}")
	public ResponseEntity<Porudzbina> deletePorudzbina(@PathVariable ("id") Integer id){
		if(!porudzbinaRepository.existsById(id))
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		jdbcTemplate.execute("delete from stavka_porudzbine where porudzbina = "+id);
		porudzbinaRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//insert - Ovde koristimo POST metodu
		@PostMapping("porudzbina")
		public ResponseEntity<Porudzbina> insertPorudzbina(@RequestBody Porudzbina porudzbina){
			if(porudzbinaRepository.existsById(porudzbina.getId())) {
				return new ResponseEntity<> (HttpStatus.CONFLICT);
			}
			porudzbinaRepository.save(porudzbina);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		
		// update - Ovde koristimo PUT metodu
		@PutMapping("porudzbina")
		public ResponseEntity<Porudzbina> updatePorudzbina(@RequestBody Porudzbina porudzbina){
			if(porudzbinaRepository.existsById(porudzbina.getId())) {
				porudzbinaRepository.save(porudzbina);
				return new ResponseEntity<> (HttpStatus.OK);
			}
			return new ResponseEntity<> (HttpStatus.NO_CONTENT);
		}
}
