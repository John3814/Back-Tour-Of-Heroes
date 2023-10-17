package co.udea.api.hero.repository;

import co.udea.api.hero.model.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Integer> {


    @Query("""
    select h from Hero h
    where h.name like %:term%
    """)
    List<Hero> searchHeroesByTerm(String term);
}
