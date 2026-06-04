package com.paypilot.ledger.storage;

import java.util.List;

public interface StorageService {
  void write(String key, byte[] data);

  byte[] read(String key);

  List<String> list(String prefix);

  void delete(String key);
}
