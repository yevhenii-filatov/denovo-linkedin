package com.dataox.linkedinscraper.parser;

import java.util.List;

public interface LinkedinParser <T,Z>{
    List<T> parse(Z source);
}
