package oogasalad.userData.parser;

import java.util.LinkedHashMap;
import java.util.Map;
import oogasalad.userData.records.UserGameData;
import oogasalad.userData.records.UserLevelData;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
    * Converts <userGameData> XML elements into UserGameData records.
    * Parses overall game stats and delegates per-level parsing to UserLevelDataParser.
    *
    * @author Billy McCune
 */
public class UserGameDataParser {

  private final UserLevelDataParser myUserLevelDataParser = new UserLevelDataParser();

  /**
   * Parses a <userGameData> element into a UserGameData record.
   *
   * @param ugdElem the XML Element representing <userGameData>
   * @return a UserGameData record with gameName, lastPlayed, highest game stats, and per-level data
   */
  public UserGameData fromElement(Element ugdElem) {
    String gameName   = getText(ugdElem, "gameName");
    String lastPlayed = getText(ugdElem, "lastPlayed");

    // parse playerHighestGameStatMap
    Element highElem = (Element)
        ugdElem.getElementsByTagName("playerHighestGameStatMap").item(0);
    Map<String, String> highMap = new LinkedHashMap<>();
    NodeList highStats = highElem.getElementsByTagName("stat");
    for (int i = 0; i < highStats.getLength(); i++) {
      Element s = (Element) highStats.item(i);
      highMap.put(
          s.getAttribute("name"),
          s.getTextContent()
      );
    }

    // parse playerLevelStatMap
    Element lvlListElem = (Element)
        ugdElem.getElementsByTagName("playerLevelStatMap").item(0);
    NodeList levels = lvlListElem.getElementsByTagName("level");
    Map<String, UserLevelData> levelMap = new LinkedHashMap<>();
    for (int i = 0; i < levels.getLength(); i++) {
      Element lvlElem = (Element) levels.item(i);
      UserLevelData uld = myUserLevelDataParser.fromElement(lvlElem);
      levelMap.put(uld.levelName(), uld);
    }

    return new UserGameData(gameName, lastPlayed, highMap, levelMap);
  }

  /**
   * Utility method to extract text content of the first occurrence of a tag.
   *
   * @param parent the Element to search within
   * @param tag the tag name whose text content is to be retrieved
   * @return the text content of the tag, or null if not present
   */
  private static String getText(Element parent, String tag) {
    NodeList list = parent.getElementsByTagName(tag);
    return (list.getLength() == 0)
        ? null
        : list.item(0).getTextContent();
  }
}