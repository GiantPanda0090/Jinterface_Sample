%%https://stackoverflow.com/questions/22593537/sending-a-message-from-java-to-a-registered-erlang-gen-server
%%lunch server with erl -sname <servename> -setcookie <cookies name>

-module(erl_genserver).
-behaviour(gen_server).

-export([start_link/0, stop/0]).
-export([init/1, handle_call/3, handle_cast/2, handle_info/2,
         terminate/2, code_change/3]).

-record(state, {}).

start_link() ->
    {ok,PID}=gen_server:start_link({global, myServerName}, ?MODULE, [], []),
    file:write_file("../config", io_lib:fwrite("PID: ~p.\n", [PID])).

stop() ->
    gen_server:cast({global, myServerName}, stop).

init([]) ->
    {ok, #state{}}.

handle_call(Request, From, State) ->
    io:format("publish call from ~p: ~p~n", [From, Request]),
    From ! "ACK",
    {reply, ok, State}.

handle_cast(stop, State) ->
    {stop, normal, State};
handle_cast(_Msg, State) ->
    {noreply, State}.

handle_info({publish, {From, Msg}}, State) ->
    %%put your server action here
    io:format("publish info for from ~p: ~p~n", [From, Msg]),
    From ! "ACK",
    {noreply, State};
handle_info(_Msg, State) ->
    io:format("publish for topic: ~p~n", [_Msg]),
    {noreply, State}.

terminate(_Reason, _State) ->
    ok.

code_change(_OldVsn, State, _Extra) ->
    {ok, State}.