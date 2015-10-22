package de.fxdiagram.core.export;

import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Data
@SuppressWarnings("all")
public class SvgLink {
  private final String href;
  
  private final String title;
  
  private final String targetFrame;
  
  private final boolean openInNewWindow;
  
  public final static SvgLink NONE = new SvgLink(null, null, null, false);
  
  public SvgLink(final String href, final String title, final String targetFrame, final boolean openInNewWindow) {
    super();
    this.href = href;
    this.title = title;
    this.targetFrame = targetFrame;
    this.openInNewWindow = openInNewWindow;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.href== null) ? 0 : this.href.hashCode());
    result = prime * result + ((this.title== null) ? 0 : this.title.hashCode());
    result = prime * result + ((this.targetFrame== null) ? 0 : this.targetFrame.hashCode());
    result = prime * result + (this.openInNewWindow ? 1231 : 1237);
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SvgLink other = (SvgLink) obj;
    if (this.href == null) {
      if (other.href != null)
        return false;
    } else if (!this.href.equals(other.href))
      return false;
    if (this.title == null) {
      if (other.title != null)
        return false;
    } else if (!this.title.equals(other.title))
      return false;
    if (this.targetFrame == null) {
      if (other.targetFrame != null)
        return false;
    } else if (!this.targetFrame.equals(other.targetFrame))
      return false;
    if (other.openInNewWindow != this.openInNewWindow)
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("href", this.href);
    b.add("title", this.title);
    b.add("targetFrame", this.targetFrame);
    b.add("openInNewWindow", this.openInNewWindow);
    return b.toString();
  }
  
  @Pure
  public String getHref() {
    return this.href;
  }
  
  @Pure
  public String getTitle() {
    return this.title;
  }
  
  @Pure
  public String getTargetFrame() {
    return this.targetFrame;
  }
  
  @Pure
  public boolean isOpenInNewWindow() {
    return this.openInNewWindow;
  }
}
