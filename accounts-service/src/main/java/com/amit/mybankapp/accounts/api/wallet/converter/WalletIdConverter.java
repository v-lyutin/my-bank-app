package com.amit.mybankapp.accounts.api.wallet.converter;

import com.amit.mybankapp.accounts.domain.wallet.vo.WalletId;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class WalletIdConverter implements Converter<String, WalletId> {

    @Override
    public WalletId convert(@NonNull String source) {
        try {
            return WalletId.of(UUID.fromString(source));
        } catch (IllegalArgumentException exception) {
            throw new ConversionFailedException(
                    TypeDescriptor.valueOf(String.class),
                    TypeDescriptor.valueOf(WalletId.class),
                    source,
                    exception
            );
        }
    }

}
