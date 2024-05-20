package example.cashcard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

// TODO what is Component actl?
@RestController // Instructs Spring that this class is a Component of type RestController and capable of handling HTTP requests.
@RequestMapping("/cashcards") // Instructs Spring to map web requests onto methods.
class CashCardController {
    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping("/{requestedId}") // Instructs Spring to map HTTP GET requests onto handler methods.
    private ResponseEntity<CashCard> findById(
            @PathVariable Long requestedId // Instructs Spring to inject URI template variable value into method parameter.
    ) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(requestedId);
        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(
            @RequestBody CashCard newCashCardRequest,
            UriComponentsBuilder ucb // Injected by Spring IoC Container
    ) {
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);
        URI locationOfNewCashCard = ucb
            .path("cashcards/{id}")
            .buildAndExpand(savedCashCard.id())
            .toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    // Must delete this mtd to prevent the err below.
    // Caused by: java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'cashCardController' method
    // example.cashcard.CashCardController#findAll(Pageable)
    // to {GET [/cashcards]}: There is already 'cashCardController' bean method
    // example.cashcard.CashCardController#findAll() mapped.
    // @GetMapping()
    // private ResponseEntity<Iterable<CashCard>> findAll() {
    //     return ResponseEntity.ok(cashCardRepository.findAll());
    // }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(Pageable pageable) { // Spring Web Pageable obj
        Page<CashCard> page = cashCardRepository.findAll(
            PageRequest.of(
                pageable.getPageNumber(), // default is 0
                pageable.getPageSize(), // default is 20
                // pageable.getSort()
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount")) // use getSortOr() to supply default value
            )); // PageRequest is a basic Java Bean implementation of Pageable TODO wdym?
        return ResponseEntity.ok(page.getContent());
    }
}
