package org.johnnuy.cesium.converter;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.stereotype.Component;

@Component
public class TrimStringConverter implements ConditionalGenericConverter {

	@Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return targetType.getAnnotation(TrimString.class) != null;
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Set.of(new ConvertiblePair(String.class, String.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {        
        String sourceValue = ((String) source);
        return StringUtils.trim(sourceValue);
    }
}