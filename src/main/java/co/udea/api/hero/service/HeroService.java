package co.udea.api.hero.service;

import co.udea.api.hero.exception.BusinessException;
import co.udea.api.hero.model.Hero;
import co.udea.api.hero.repository.HeroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class HeroService {

    private final Logger log = LoggerFactory.getLogger(HeroService.class);

    private HeroRepository heroRepository;

    public HeroService(HeroRepository heroRepository){
        this.heroRepository = heroRepository;
    }
    public Hero getHero(Integer id){
        Optional<Hero> optionalHero = heroRepository.findById(id);
        if(!optionalHero.isPresent()){
            log.info("No se encuentra un heroe con ID: "+id);
            throw new BusinessException("El heroe no existe");
        }
        return optionalHero.get();
    }


    public List<Hero> getHeroes() {
        return this.heroRepository.findAll();
    }

    public List<Hero> searchHeroes(String term) {
        return this.heroRepository.searchHeroesByTerm(term);
    }

    public Hero updateHero(Hero hero) {
        Optional<Hero> optionalHero = this.heroRepository.findById(hero.getId());
        if (optionalHero.isEmpty()) {
            log.info("No se encuentra un heroe con ID: " + hero.getId());
            throw new BusinessException("El heroe no existe");
        }

        Hero hero1 = optionalHero.get();
        hero1.setName(hero.getName());
        return hero1;
    }

    public Hero addHero(Hero hero) {
        return this.heroRepository.save(hero);
    }

    public void deleteHero(Integer id) {
        this.heroRepository.deleteById(id);
    }
}
