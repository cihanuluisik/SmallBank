package com.sbank.service.international;

import com.sbank.domain.international.InternationalTransferEvent;

public interface InternationalTransferEventListener {
    void receive(InternationalTransferEvent internationalTransferEvent);
}
