package com.dataox.linkedinscraper.parser;

public interface LinkedinParser<T, Z> {
    T parse(Z source);
}
