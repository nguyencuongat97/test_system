package com.foxconn.fii.data.primary.model.entity;

import com.foxconn.fii.common.ListColumnConverter;
import com.foxconn.fii.data.MoType;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "test_mo_type_config")
public class TestMoTypeConfig {

    @Id
    private int id;

    @Column(name = "factory")
    private String factory;

    @Enumerated(EnumType.STRING)
    @Column(name = "mo_type")
    private MoType moType = MoType.ALL;

    @Column(name = "prefixes")
    @Convert(converter = ListColumnConverter.class)
    private List<String> prefixes = new ArrayList<>();

    public boolean contain(String mo) {
        if (StringUtils.isEmpty(mo) || moType == MoType.ALL) {
            return true;
        }

        for (String prefix : prefixes) {
            if (mo.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
