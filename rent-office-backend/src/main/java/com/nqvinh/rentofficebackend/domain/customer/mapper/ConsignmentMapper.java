package com.nqvinh.rentofficebackend.domain.customer.mapper;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {ConsignmentImageMapper.class})
public interface ConsignmentMapper extends CommonMapper<ConsignmentDto, Consignment> {
//    @Override
//    @Mapping(target = "consignmentImages", expression = "java(updateConsignmentImages(entity, dto.getConsignmentImages()))")
//    void partialUpdate(@MappingTarget Consignment entity, ConsignmentDto dto);
//
//    default List<ConsignmentImage> updateConsignmentImages(Consignment consignment, List<ConsignmentImageDto> consignmentImageDto) {
//        if (consignmentImageDto == null || consignmentImageDto.isEmpty()) {
//            return consignment.getConsignmentImages();
//        }
//
//        List<String> newUrls = consignmentImageDto.stream()
//                .map(ConsignmentImageDto::getImgUrl)
//                .collect(Collectors.toList());
//
//        consignment.getConsignmentImages().removeIf(image -> !newUrls.contains(image.getImgUrl()));
//
//        consignmentImageDto.forEach(img -> {
//            consignment.getConsignmentImages().stream()
//                    .filter(image -> image.getImgUrl().equals(img.getImgUrl()))
//                    .findFirst()
//                    .orElseGet(() -> {
//                        ConsignmentImage newImage = ConsignmentImage.builder()
//                                .consignmentImageId(img.getConsignmentImageId())
//                                .imgUrl(img.getImgUrl())
//                                .consignment(consignment)
//                                .build();
//                        consignment.getConsignmentImages().add(newImage);
//                        return newImage;
//                    });
//        });
//
//        return consignment.getConsignmentImages();
//    }
}
