package com.fga.production.presentation;

import com.fga.production.application.port.in.ConsulterBordereauxUseCase;
import com.fga.production.application.port.in.ReceptionnerBordereauUseCase;
import com.fga.production.presentation.dto.BordereauRequest;
import com.fga.production.presentation.dto.BordereauResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bordereaux")
public class BordereauController {

    private final ReceptionnerBordereauUseCase receptionnerUseCase;
    private final ConsulterBordereauxUseCase consulterUseCase;

    public BordereauController(ReceptionnerBordereauUseCase receptionnerUseCase,
                                ConsulterBordereauxUseCase consulterUseCase) {
        this.receptionnerUseCase = receptionnerUseCase;
        this.consulterUseCase = consulterUseCase;
    }

    @PostMapping
    public ResponseEntity<BordereauResponse> receptionner(@Valid @RequestBody BordereauRequest request) {
        var bordereau = receptionnerUseCase.receptionnerBordereau(
                request.getCompagnieAssurance(), request.getMontantPrimesCollectees());
        return ResponseEntity.status(HttpStatus.CREATED).body(BordereauResponse.depuis(bordereau));
    }

    @GetMapping
    public List<BordereauResponse> lister() {
        return consulterUseCase.listerBordereaux().stream().map(BordereauResponse::depuis).toList();
    }
}
