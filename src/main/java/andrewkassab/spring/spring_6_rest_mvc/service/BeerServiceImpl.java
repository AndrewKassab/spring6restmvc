package andrewkassab.spring.spring_6_rest_mvc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import andrewkassab.spring.spring_6_rest_mvc.entity.Beer;
import andrewkassab.spring.spring_6_rest_mvc.mapper.BeerMapper;
import andrewkassab.spring.spring_6_rest_mvc.model.BeerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import andrewkassab.spring.spring_6_rest_mvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
	
	BeerMapper mapper;

	private Map<UUID, BeerDTO> beerMap;
	
	public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356222")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
		
	}
	
	public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {
		return new PageImpl<>(new ArrayList<>(beerMap.values()));
	}
	
	public Optional<BeerDTO> getBeerById(UUID id) {
		
		log.debug("Get Beer Id in service was called");
		
		return Optional.of(beerMap.get(id));

	}
	
	public BeerDTO saveNewBeer(BeerDTO beer) {
		beer.setId(UUID.randomUUID());
		beer.setCreatedDate(LocalDateTime.now());
		beer.setUpdateDate(LocalDateTime.now());
		
		beerMap.put(beer.getId(), beer);
		
		return beer;
	}

	@Override
	public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {
		BeerDTO existing = beerMap.get(beerId);
		existing.setBeerName(beer.getBeerName());
		existing.setPrice(beer.getPrice());
		existing.setUpc(beer.getUpc());
		existing.setQuantityOnHand(beer.getQuantityOnHand());
		existing.setBeerStyle(beer.getBeerStyle());
		return Optional.of(existing);
	}

	@Override
	public boolean deleteById(UUID beerId) {
		beerMap.remove(beerId);
		return true;
	}

	@Override
	public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO updatedBeer) {
		Beer existing = mapper.beerDtoToBeer(beerMap.get(beerId));

		if (existing != null) {
			mapper.updateBeerFromDto(existing, updatedBeer);
		}
		
		existing.setUpdateDate(LocalDateTime.now());
		return Optional.of(mapper.beerToBeerDto(existing));
	}

}
