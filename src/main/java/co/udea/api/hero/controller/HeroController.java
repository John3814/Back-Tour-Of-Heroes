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
    @ApiOperation(value = "Obtener todos los héroes", response = Hero.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de héroes recuperada exitosamente"),
            @ApiResponse(code = 500, message = "Error interno al procesar la respuesta")
    })
    public ResponseEntity<List<Hero>> getHeroes(){
        return ResponseEntity.ok().body(this.heroService.getHeroes());
    }

    @GetMapping("/")
    @ApiOperation(value = "Buscar héroes por nombre", response = Hero.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de héroes encontrada exitosamente"),
            @ApiResponse(code = 400, message = "Solicitud inválida"),
            @ApiResponse(code = 500, message = "Error interno al procesar la respuesta")
    })
    public ResponseEntity<List<Hero>> searchHeroes(@RequestParam("name") String term){
        log.info("Rest request buscar heroe por term: " + term);
        return ResponseEntity.ok().body(this.heroService.searchHeroes(term));
    }

    @PutMapping
    @Transactional
    @ApiOperation(value = "Actualizar un héroe", response = Hero.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Héroe actualizado exitosamente"),
            @ApiResponse(code = 400, message = "Solicitud inválida"),
            @ApiResponse(code = 500, message = "Error interno al procesar la respuesta")
    })
    public ResponseEntity<Hero> updateHero(@RequestBody Hero hero){
        return ResponseEntity.ok().body(this.heroService.updateHero(hero));
    }

    @PostMapping
    @ApiOperation(value = "Agregar un nuevo héroe", response = Hero.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Héroe creado exitosamente"),
            @ApiResponse(code = 400, message = "Solicitud inválida"),
            @ApiResponse(code = 500, message = "Error interno al procesar la respuesta")
    })
    public ResponseEntity<Hero> addHero(@RequestBody Hero hero, UriComponentsBuilder uriComponentsBuilder){
        Hero hero1 = this.heroService.addHero(hero);
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(hero1.getId()).toUri();
        return ResponseEntity.created(url).body(hero1);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar un héroe por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Héroe eliminado exitosamente"),
            @ApiResponse(code = 404, message = "Héroe no encontrado"),
            @ApiResponse(code = 500, message = "Error interno al procesar la solicitud")
    })
    public ResponseEntity deleteHero(@PathVariable Integer id){
        this.heroService.deleteHero(id);
        return  ResponseEntity.noContent().build();
    }
}
