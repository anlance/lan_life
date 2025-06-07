package club.anlan.lanlife.proxy.server.demo.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.converters.AutoConverter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.HeadKindEnum;
import com.alibaba.excel.exception.ExcelCommonException;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.Holder;
import com.alibaba.excel.metadata.property.DateTimeFormatProperty;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.metadata.property.ExcelHeadProperty;
import com.alibaba.excel.metadata.property.NumberFormatProperty;
import com.alibaba.excel.util.ClassUtils;
import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.excel.util.CustomClassUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.holder.AbstractWriteHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 类
 *
 * @author lan
 * @version 1.0
 * @date 2025/3/15 14:02
 */
public class CustomExcelHeadProperty extends ExcelHeadProperty {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelHeadProperty.class);

    public CustomExcelHeadProperty(Holder holder,  Class headClazz, List<List<String>> head, Boolean convertAllFiled) {
        super(holder, headClazz, head, convertAllFiled);
        reInitColumnProperties(holder, convertAllFiled);

        reInitHeadRowNumber();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("(custom) The initialization sheet/table 'ExcelHeadProperty' is complete , head kind is {}", this.getHeadKind());
        }
    }

    private void reInitColumnProperties(Holder holder, Boolean convertAllFiled) {
        if (this.getHeadClazz() == null) {
            return;
        }
        // Declared fields
        Map<Integer, Field> sortedAllFiledMap = new TreeMap<Integer, Field>();
        Map<Integer, Field> indexFiledMap = new TreeMap<Integer, Field>();

        boolean needIgnore = (holder instanceof AbstractWriteHolder) && (
                !CollectionUtils.isEmpty(((AbstractWriteHolder) holder).getExcludeColumnFiledNames()) || !CollectionUtils
                        .isEmpty(((AbstractWriteHolder) holder).getExcludeColumnIndexes()) || !CollectionUtils
                        .isEmpty(((AbstractWriteHolder) holder).getIncludeColumnFiledNames()) || !CollectionUtils
                        .isEmpty(((AbstractWriteHolder) holder).getIncludeColumnIndexes()));
        // 重点，自定义
        CustomClassUtils.declaredFields(this.getHeadClazz(), sortedAllFiledMap, indexFiledMap, this.getIgnoreMap(), convertAllFiled, needIgnore,
                holder, 1);

        for (Map.Entry<Integer, Field> entry : sortedAllFiledMap.entrySet()) {
            reInitOneColumnProperty(entry.getKey(), entry.getValue(), indexFiledMap.containsKey(entry.getKey()));
        }
        this.setHeadKind(HeadKindEnum.CLASS);
    }

    private void reInitHeadRowNumber() {
        this.setHeadRowNumber(0);
        for (Head head : this.getHeadMap().values()) {
            List<String> list = head.getHeadNameList();
            if (list != null && list.size() > this.getHeadRowNumber()) {
                this.setHeadRowNumber(list.size());
            }
        }
        for (Head head : this.getHeadMap().values()) {
            List<String> list = head.getHeadNameList();
            if (list != null && !list.isEmpty() && list.size() < this.getHeadRowNumber()) {
                int lack = this.getHeadRowNumber() - list.size();
                int last = list.size() - 1;
                for (int i = 0; i < lack; i++) {
                    list.add(list.get(last));
                }
            }
        }
    }

    private void reInitOneColumnProperty(int index, Field field, Boolean forceIndex) {
        ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
        List<String> tmpHeadList = new ArrayList<String>();
        boolean notForceName = excelProperty == null || excelProperty.value().length <= 0
                || (excelProperty.value().length == 1 && StringUtils.isEmpty((excelProperty.value())[0]));
        if (this.getHeadMap().containsKey(index)) {
            tmpHeadList.addAll(this.getHeadMap().get(index).getHeadNameList());
        } else {
            if (notForceName) {
                tmpHeadList.add(field.getName());
            } else {
                Collections.addAll(tmpHeadList, excelProperty.value());
            }
        }
        Head head = new Head(index, field.getName(), tmpHeadList, forceIndex, !notForceName);
        ExcelContentProperty excelContentProperty = new ExcelContentProperty();
        if (excelProperty != null) {
            Class<? extends Converter> convertClazz = excelProperty.converter();
            if (convertClazz != AutoConverter.class) {
                try {
                    Converter converter = convertClazz.newInstance();
                    excelContentProperty.setConverter(converter);
                } catch (Exception e) {
                    throw new ExcelCommonException("Can not instance custom converter:" + convertClazz.getName());
                }
            }
        }
        excelContentProperty.setHead(head);
        excelContentProperty.setField(field);
        excelContentProperty
                .setDateTimeFormatProperty(DateTimeFormatProperty.build(field.getAnnotation(DateTimeFormat.class)));
        excelContentProperty
                .setNumberFormatProperty(NumberFormatProperty.build(field.getAnnotation(NumberFormat.class)));
        this.getHeadMap().put(index, head);
        this.getContentPropertyMap().put(index, excelContentProperty);
        this.getFieldNameContentPropertyMap().put(field.getName(), excelContentProperty);
    }
}
