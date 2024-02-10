package transcribe.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class AwsTranscriptResult {
    // {"jobName":"7a4c292d-a221-481f-9354-4aea6f463fed","accountId":"778760699735","status":"COMPLETED","results":{"transcripts":[{"transcript":"It
    // was not like I was asking for the code to a nuclear bunker or anything like
    // that, but the amount of resistance I got
    // from."}],"items":[{"type":"pronunciation","alternatives":[{"confidence":"0.997","content":"It"}],"start_time":"0.409","end_time":"0.589"},{"type":"pronunciation","alternatives":[{"confidence":"0.873","content":"was"}],"start_time":"0.6","end_time":"0.709"},{"type":"pronunciation","alternatives":[{"confidence":"0.808","content":"not"}],"start_time":"0.72","end_time":"0.829"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"like"}],"start_time":"0.839","end_time":"1.08"},{"type":"pronunciation","alternatives":[{"confidence":"0.999","content":"I"}],"start_time":"1.09","end_time":"1.12"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"was"}],"start_time":"1.129","end_time":"1.289"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"asking"}],"start_time":"1.299","end_time":"1.669"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"for"}],"start_time":"1.679","end_time":"1.789"},{"type":"pronunciation","alternatives":[{"confidence":"0.996","content":"the"}],"start_time":"1.799","end_time":"1.879"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"code"}],"start_time":"1.889","end_time":"2.47"},{"type":"pronunciation","alternatives":[{"confidence":"0.996","content":"to"}],"start_time":"2.48","end_time":"2.66"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"a"}],"start_time":"2.67","end_time":"2.68"},{"type":"pronunciation","alternatives":[{"confidence":"0.999","content":"nuclear"}],"start_time":"2.69","end_time":"3.049"},{"type":"pronunciation","alternatives":[{"confidence":"0.997","content":"bunker"}],"start_time":"3.059","end_time":"3.44"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"or"}],"start_time":"3.45","end_time":"3.539"},{"type":"pronunciation","alternatives":[{"confidence":"0.999","content":"anything"}],"start_time":"3.549","end_time":"3.839"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"like"}],"start_time":"3.849","end_time":"3.97"},{"type":"pronunciation","alternatives":[{"confidence":"0.997","content":"that"}],"start_time":"3.98","end_time":"4.179"},{"type":"punctuation","alternatives":[{"confidence":"0.0","content":","}]},{"type":"pronunciation","alternatives":[{"confidence":"0.997","content":"but"}],"start_time":"4.19","end_time":"4.269"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"the"}],"start_time":"4.28","end_time":"4.36"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"amount"}],"start_time":"4.369","end_time":"4.65"},{"type":"pronunciation","alternatives":[{"confidence":"0.998","content":"of"}],"start_time":"4.659","end_time":"4.76"},{"type":"pronunciation","alternatives":[{"confidence":"0.997","content":"resistance"}],"start_time":"4.769","end_time":"5.51"},{"type":"pronunciation","alternatives":[{"confidence":"0.997","content":"I"}],"start_time":"5.8","end_time":"5.949"},{"type":"pronunciation","alternatives":[{"confidence":"0.996","content":"got"}],"start_time":"5.96","end_time":"6.23"},{"type":"pronunciation","alternatives":[{"confidence":"0.996","content":"from"}],"start_time":"6.239","end_time":"6.51"},{"type":"punctuation","alternatives":[{"confidence":"0.0","content":"."}]}]}}
    private final String transcript;
}
