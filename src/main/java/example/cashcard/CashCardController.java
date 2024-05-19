package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO what is Component actl?
@RestController // Instructs Spring that this class is a Component of type RestController and capable of handling HTTP requests.
@RequestMapping("/cashcards") // Instructs Spring to map web requests onto methods.
class CashCardController {

    @GetMapping("/{requestedId}") // Instructs Spring to map HTTP GET requests onto handler methods.
    private ResponseEntity<CashCard> findById(
            @PathVariable Long requestedId // Instructs Spring to inject URI template variable value into method parameter.
    ) {
        if (requestedId.equals(99L)) {
            CashCard cashCard = new CashCard(99L, 123.45);
            return ResponseEntity.ok(cashCard);
        }
        return ResponseEntity.notFound().build();
    }
}
