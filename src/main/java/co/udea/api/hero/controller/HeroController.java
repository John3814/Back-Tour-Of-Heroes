package co.udea.api.hero.controller;

import co.udea.api.hero.model.Hero;
import co.udea.api.hero.service.HeroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/heroes")
public class HeroController {

    private final Logger log = LoggerFactory.getLogger(HeroController.class);

    private HeroService heroService;

    public HeroController(HeroService heroService){
        this.heroService = heroService;
    }

    @GetMapping("{id}")
    @ApiOperation(value = "Busca un hero por su id",  response = Hero.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Hero encontrado existosamente"),
            @ApiResponse(code = 400, message = "La petición es invalida"),
            @ApiResponse(code = 500, message = "Error interno al procesar la respuesta")})
    public ResponseEntity<Hero> getHero(@PathVariable Integer id){
        log.info("Rest request buscar heroe por id: " + id);
        return ResponseEntity.ok(heroService.getHero(id));
    }

    @GetMapping
    public ResponseEntity<List<Hero>> getHeroes(){
        return ResponseEntity.ok().body(this.heroService.getHeroes());
    }

    @GetMapping("/")
    public ResponseEntity<List<Hero>> searchHeroes(@RequestParam("name") String term){
        log.info("Rest request buscar heroe por term: " + term);
        return ResponseEntity.ok().body(this.heroService.searchHeroes(term));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Hero> updateHero(@RequestBody Hero hero){
        return ResponseEntity.ok().body(this.heroService.updateHero(hero));
    }

    @PostMapping
    public ResponseEntity<Hero> addHero(@RequestBody Hero hero, UriComponentsBuilder uriComponentsBuilder){
        Hero hero1 = this.heroService.addHero(hero);
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(hero1.getId()).toUri();
        return ResponseEntity.created(url).body(hero1);
    }
}
