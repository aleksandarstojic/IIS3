package rva.ctrls;

import java.math.BigDecimal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import rva.jpa.Porudzbina;
import rva.jpa.StavkaPorudzbine;
import rva.reps.PorudzbinaRepository;
import rva.reps.StavkaPorudzbineRepository;

@RestController
public class StavkaPorudzbineRestController {
	//@Api(tags = {"Stavka porudÅ¾bine CRUD operacije"})
	
		@Autowired
		private StavkaPorudzbineRepository stavkaPorudzbineRepository;
		@Autowired
		private PorudzbinaRepository porudzbinaRepository;

		@GetMapping(value = "stavkaPorudzbine")
		//@ApiOperation(value = "VraÄ‡a kolekciju svih stavki porudÅ¾bina iz baze podataka")
		public Collection<StavkaPorudzbine> getStavkePorudzbine(){
			return stavkaPorudzbineRepository.findAll();
		}

		@GetMapping(value = "stavkaPorudzbineId/{id}")
		//@ApiOperation(value = "VraÄ‡a stavku porudÅ¾bine iz baze podataka Ä�iji je id vrednost prosleÄ‘ena kao path varijabla")
		public ResponseEntity<StavkaPorudzbine> getStavkaPorudzbine(@PathVariable("id") Integer id){
			StavkaPorudzbine stavkaPorudzbine = stavkaPorudzbineRepository.getOne(id);
			return new ResponseEntity<StavkaPorudzbine>(stavkaPorudzbine, HttpStatus.OK);
		}

		@GetMapping(value = "stavkeZaPorudzbinaId/{id}")
		//@ApiOperation(value = "VraÄ‡a sve stavke porudÅ¾bine iz baze podataka vezane za porudÅ¾binu Ä�iji je id vrednost prosleÄ‘ena kao path varijabla")
		public Collection<StavkaPorudzbine> stavkaPoPorudzbiniId(@PathVariable("id") int id){
			Porudzbina p = porudzbinaRepository.getOne(id);
			return stavkaPorudzbineRepository.findByPorudzbina(p);
		}
		
		@GetMapping(value = "stavkaPorudzbineCena/{cena}")
		public Collection<StavkaPorudzbine> getStavkaPorudzbineCena(@PathVariable("cena") BigDecimal cena){
			return stavkaPorudzbineRepository.findByCenaLessThanOrderById(cena);
		}

		@CrossOrigin
		@DeleteMapping (value = "stavkaPorudzbineId/{id}")
		//@ApiOperation(value = "BriÅ¡e stavku porudÅ¾bine iz baze podataka Ä�iji je id vrednost prosleÄ‘ena kao path varijabla")
		public ResponseEntity<StavkaPorudzbine> deleteStavkaPorudzbine(@PathVariable("id") Integer id){
			if(!stavkaPorudzbineRepository.existsById(id))
				return new ResponseEntity<StavkaPorudzbine>(HttpStatus.NO_CONTENT);
			stavkaPorudzbineRepository.deleteById(id);
			return new ResponseEntity<StavkaPorudzbine>(HttpStatus.OK);
		}

		//insert
		@CrossOrigin
		@PostMapping(value = "stavkaPorudzbine")
		//@ApiOperation(value = "Upisuje stavku porudÅ¾bine u bazu podataka")
		public ResponseEntity<Void> insertStavkaPorudzbine(@RequestBody StavkaPorudzbine stavkaPorudzbine){
			if(stavkaPorudzbineRepository.existsById(stavkaPorudzbine.getId()))
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			stavkaPorudzbine.setRedniBroj(stavkaPorudzbineRepository.nextRBr(stavkaPorudzbine.getPorudzbina().getId()));
			stavkaPorudzbineRepository.save(stavkaPorudzbine);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}

		//update
		@CrossOrigin
		@PutMapping(value = "stavkaPorudzbine")
		//@ApiOperation(value = "Modifikuje postojeÄ‡u stavku porudÅ¾bine u bazi podataka")
		public ResponseEntity<Void> updateStavkaPorudzbine(@RequestBody StavkaPorudzbine stavkaPorudzbine){
			if(!stavkaPorudzbineRepository.existsById(stavkaPorudzbine.getId()))
				return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
			stavkaPorudzbineRepository.save(stavkaPorudzbine);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	}
