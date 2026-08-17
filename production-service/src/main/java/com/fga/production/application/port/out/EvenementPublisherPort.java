package com.fga.production.application.port.out;

import com.fga.production.domain.event.BordereauRecuEvent;

public interface EvenementPublisherPort {
    void publierBordereauRecu(BordereauRecuEvent evenement);
}
