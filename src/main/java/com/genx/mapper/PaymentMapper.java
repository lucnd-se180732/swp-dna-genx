package com.genx.mapper;




import com.genx.dto.PaymentDTO;
import com.genx.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "transactionNo", target = "transactionNo")
    @Mapping(source = "responseCode", target = "responseCode")
    @Mapping(source = "payDate", target = "payDate")
    PaymentDTO toDTO(Payment payment);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "transactionNo", target = "transactionNo")
    @Mapping(source = "responseCode", target = "responseCode")
    @Mapping(source = "payDate", target = "payDate")
    Payment toEntity(PaymentDTO paymentDTO);
}